package labs.com.usptodatabasegenerator.uspto.domain.service;

import labs.com.usptodatabasegenerator.uspto.domain.entity.file.UsptoFile;
import labs.com.usptodatabasegenerator.uspto.dto.JobStatusDTO;

import java.time.LocalDate;
import java.util.List;

public interface DownloadService {

    List<UsptoFile> getFiles();

    UsptoFile getCurrentUsptoFileOnWorking();

    Boolean runDownloadJob(LocalDate initialDate, LocalDate finalDate);

    JobStatusDTO getJobStatusDTO();

    Boolean stopJob();
}
