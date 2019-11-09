package labs.com.usptodatabasegenerator.uspto.domain.service.impl;

import labs.com.usptodatabasegenerator.uspto.domain.dao.file.UsptoFileRepository;
import labs.com.usptodatabasegenerator.uspto.domain.entity.file.UsptoFile;
import labs.com.usptodatabasegenerator.uspto.domain.service.UsptoFileService;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class UsptoFileServiceImpl implements UsptoFileService {

    private final UsptoFileRepository usptoFileRepository;
    private final Logger LOG = LoggerFactory.getLogger(this.getClass());

    public UsptoFileServiceImpl(UsptoFileRepository usptoFileRepository) {
        this.usptoFileRepository = usptoFileRepository;
    }

    @Override
    public Map<String, UsptoFile> getAllUsptoFileFromRepository() {

    return usptoFileRepository
                .findAll()
                .stream()
                .collect(Collectors.toMap(UsptoFile::getName, Function.identity()));
    }

    @Override
    public void save(UsptoFile usptoFile) {
        usptoFileRepository.save(usptoFile);
    }

    @Override
    public UsptoFile findByName(File file) {
        return usptoFileRepository.findOneByName(file.getName());
    }

    @Override
    public void deleteFile(UsptoFile usptoFile, Path path) {
        LOG.info("Deletando " + usptoFile.getName());
        FileUtils.deleteQuietly(path.toFile());
        usptoFileRepository.delete(usptoFile);
    }

    @Override
    public void setAsProcessedFromUnzipedFile(Path pathFile) {
        String fileName = pathFile.getFileName().toString();
        fileName = fileName.replace("xml", "zip");

        UsptoFile file = usptoFileRepository
                .findOneByName(fileName);
        file.setProcessDate(LocalDateTime.now());
    }
}
