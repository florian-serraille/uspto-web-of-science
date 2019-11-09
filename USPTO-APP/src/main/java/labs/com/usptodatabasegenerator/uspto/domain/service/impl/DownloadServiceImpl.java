package labs.com.usptodatabasegenerator.uspto.domain.service.impl;

import labs.com.usptodatabasegenerator.uspto.batch.JobEnum;
import labs.com.usptodatabasegenerator.uspto.batch.JobRunner;
import labs.com.usptodatabasegenerator.uspto.batch.download.DownloadWriter;
import labs.com.usptodatabasegenerator.uspto.domain.dao.file.UsptoFileRepository;
import labs.com.usptodatabasegenerator.uspto.domain.entity.file.UsptoFile;
import labs.com.usptodatabasegenerator.uspto.domain.service.DownloadService;
import labs.com.usptodatabasegenerator.uspto.dto.JobStatusDTO;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

@Service
public class DownloadServiceImpl implements DownloadService {

    private final UsptoFileRepository usptoFileRepository;
    private final DownloadWriter downloadWriter;
    private final JobRunner jobRunner;

    private JobExecution downloadJobExecution;

    public DownloadServiceImpl(UsptoFileRepository usptoFileRepository,
                               DownloadWriter downloadWriter,
                               JobRunner jobRunner) {
        this.usptoFileRepository = usptoFileRepository;
        this.downloadWriter = downloadWriter;
        this.jobRunner = jobRunner;
    }

    /* -------------------------------------------------------------------------------------------------------------- */

    @Override
    public List<UsptoFile> getFiles() {
        return usptoFileRepository.findAllByOrOrderByPublicationDateDesc();
    }

    /* -------------------------------------------------------------------------------------------------------------- */

    @Override
    public UsptoFile getCurrentUsptoFileOnWorking() {
        return downloadWriter.getCurrentUsptoFile();
    }

    /* -------------------------------------------------------------------------------------------------------------- */

    @Override
    public Boolean runDownloadJob(LocalDate initialDate, LocalDate finalDate) {

        Boolean result;

        if (jobRunner.checkJobAlreadyRunning(downloadJobExecution)) {
            result = Boolean.FALSE;
        } else {
            downloadJobExecution = null;
            JobParametersBuilder jobParametersBuilder = setJobParameters(initialDate, finalDate);
            downloadJobExecution = jobRunner.run(JobEnum.DOWNLOAD, jobParametersBuilder.toJobParameters());
            result = (downloadJobExecution != null) ? Boolean.TRUE : Boolean.FALSE;
        }

        return result;
    }

    /* -------------------------------------------------------------------------------------------------------------- */

    @Override
    public JobStatusDTO getJobStatusDTO() {
        return jobRunner.getJobStatusDTO(downloadJobExecution);
    }

    /* -------------------------------------------------------------------------------------------------------------- */

    @Override
    public Boolean stopJob() {
        return jobRunner.stop(downloadJobExecution);
    }

    /* -------------------------------------------------------------------------------------------------------------- */

    private JobParametersBuilder setJobParameters(LocalDate initialDate, LocalDate finalDate) {
        JobParametersBuilder jobParametersBuilder = new JobParametersBuilder();
        if (initialDate != null) {
            jobParametersBuilder.addString("initialDate", initialDate.format(DateTimeFormatter.ISO_DATE));
        }
        if (finalDate != null) {
            jobParametersBuilder.addString("finalDate", finalDate.format(DateTimeFormatter.ISO_DATE));
        }

        jobParametersBuilder.addDate("timestamp", new Date());
        return jobParametersBuilder;
    }
}
