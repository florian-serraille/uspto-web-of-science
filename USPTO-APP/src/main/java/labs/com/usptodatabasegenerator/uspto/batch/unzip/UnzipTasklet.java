package labs.com.usptodatabasegenerator.uspto.batch.unzip;

import labs.com.usptodatabasegenerator.uspto.domain.dao.file.UsptoFileRepository;
import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Component
public class UnzipTasklet implements Tasklet {

	public static Long totalItemNumber;
	public static Long currentItem;
	private final Logger LOG = LoggerFactory.getLogger(this.getClass());
	private final Path downloadDirectory;
	private final Path unzipDirectory;
	private final UsptoFileRepository usptoFileRepository;

	public UnzipTasklet(@Value("${directory.download}") String downloadPath,
	                    @Value("${directory.unzip}") String unzipPath,
						UsptoFileRepository usptoFileRepository) {
		this.downloadDirectory = Paths.get(downloadPath);
		this.unzipDirectory = Paths.get(unzipPath);
		this.usptoFileRepository = usptoFileRepository;
		currentItem = 0L;
	}

	@Override
	public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {


		totalItemNumber = Files.list(downloadDirectory).count();
	    Files.list(downloadDirectory).forEach(this::unzip);

        return RepeatStatus.FINISHED;
	}

	private void unzip(Path path) {

		try {
			LOG.info("Unzip of " + path);
			ZipFile zipFile = new ZipFile(path.toFile());
			zipFile.extractAll(unzipDirectory.toString());
			LOG.info("Sucess !");

		} catch (ZipException e) {
			LOG.warn("Fail to unzip... " + e.getMessage(), e);
			usptoFileRepository.deleteByName(path.getFileName().toString());

		} finally {
			currentItem++;
			try {
				LOG.info("Deleting " + path);
				Files.delete(path);
			} catch (IOException e) {
				LOG.warn("Fail to delete " + path, e);
			}
		}
	}
}
