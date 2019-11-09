package labs.com.usptodatabasegenerator.uspto.batch.download;

import labs.com.usptodatabasegenerator.uspto.domain.dao.file.UsptoFileRepository;
import labs.com.usptodatabasegenerator.uspto.domain.entity.file.UsptoFile;
import labs.com.usptodatabasegenerator.uspto.domain.service.UsptoFileService;
import labs.com.usptodatabasegenerator.uspto.util.SpringContextUtil;
import labs.com.usptodatabasegenerator.uspto.util.ZipUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Component
public class DownloadWriter implements ItemWriter<UsptoFile>, StepExecutionListener {

	public static Long currentItem;
	private final Logger LOG = LoggerFactory.getLogger(this.getClass());
	private final UsptoFileService usptoFileService;
	private final RestTemplateBuilder restTemplate;
	private final Path downloadPath;
	
	private UsptoFile currentUsptoFile;

	public DownloadWriter(@Value("${directory.download}") String downloadDirectory,
						  UsptoFileRepository usptoFileRepository,
						  UsptoFileService usptoFileService, RestTemplateBuilder restTemplate) {
		this.usptoFileService = usptoFileService;
		this.restTemplate = restTemplate;
		this.downloadPath = Paths.get(downloadDirectory);
		currentItem = 0L;
	}

	@Override
	public void write(List<? extends UsptoFile> usptoFileList) {

		Map<String, UsptoFile> usptoFileMap = usptoFileService.getAllUsptoFileFromRepository();

		usptoFileList.stream()
				.filter(uspto -> !usptoFileMap.containsKey((uspto).getName()))
				.peek(uspto -> LOG.info("Download..." + uspto.getUrl()))
				.filter(this::downloadFile)
				.peek(uspto -> uspto.setDownloadDate(LocalDateTime.now()))
				.forEach(usptoFileService::save);

		currentItem++;
	}

	private Boolean downloadFile(UsptoFile uspto) {

		currentUsptoFile = uspto;
		String url = uspto.getUrl();

		try {
			HttpHeaders headers = new HttpHeaders();
			HttpEntity<String> entity = new HttpEntity<>(headers);
			ResponseEntity<byte[]> response = restTemplate.build().exchange(url, HttpMethod.GET, entity, byte[].class);
			Path path = Paths.get(downloadPath.toString(), uspto.getName());
			Files.write(path, Objects.requireNonNull(response.getBody()));
			return Boolean.TRUE;

		} catch (Exception e) {
			LOG.warn(e.getMessage());
			return Boolean.FALSE;

		} finally {
			currentUsptoFile = null;
		}
	}

	@Override
	public void beforeStep(StepExecution stepExecution) {}

	@Override
	public ExitStatus afterStep(StepExecution stepExecution) {

		try {

			List<File> files = new ArrayList<>();
			for (Resource resource : SpringContextUtil.getResources("file:" + downloadPath.toString() + "/*.zip")) {
				File resourceFile = resource.getFile();
				files.add(resourceFile);
			}

			files.stream()
					.filter(file -> !Files.isDirectory(Paths.get(file.toURI())) && isCorrupted(file))
					.map(usptoFileService::findByName)
					.filter(this::wasNotProcessed)
					.peek(file -> LOG.info(file.getName() + " corrupted and not precessed"))
					.forEach(uspto -> usptoFileService.deleteFile(uspto, Paths.get(downloadPath.toString(), uspto.getName())));

		} catch (IOException e) {

			e.printStackTrace();
			return ExitStatus.FAILED;
		}

		return stepExecution.getExitStatus();
	}

	private boolean wasNotProcessed(UsptoFile usptoFile) {
		if (usptoFile == null) return Boolean.FALSE;
		return !usptoFile.hasBeenProcessed();
	}

	private Boolean isCorrupted(File file) {

		LOG.info("Checking integrity of " + file);
		if (!ZipUtil.isValid(file)) {
			LOG.error(file + " corrupted");
			return Boolean.TRUE;
		} else {
			LOG.info(file + " is ok");
			return Boolean.FALSE;
		}
	}

	public UsptoFile getCurrentUsptoFile() {
		return currentUsptoFile;
	}
}
