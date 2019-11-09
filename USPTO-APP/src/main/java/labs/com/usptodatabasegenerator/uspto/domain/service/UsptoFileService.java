package labs.com.usptodatabasegenerator.uspto.domain.service;

import labs.com.usptodatabasegenerator.uspto.domain.entity.file.UsptoFile;

import java.io.File;
import java.nio.file.Path;
import java.util.Map;

public interface UsptoFileService {

    Map<String, UsptoFile> getAllUsptoFileFromRepository();
    void save(UsptoFile usptoFile);
    UsptoFile findByName(File file);
    void deleteFile(UsptoFile file, Path path);
    void setAsProcessedFromUnzipedFile(Path pathFile);
}
