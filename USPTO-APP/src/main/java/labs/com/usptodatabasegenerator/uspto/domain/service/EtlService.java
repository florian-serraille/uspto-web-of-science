package labs.com.usptodatabasegenerator.uspto.domain.service;

import labs.com.usptodatabasegenerator.uspto.batch.JobEnum;
import labs.com.usptodatabasegenerator.uspto.batch.JobRunner;
import labs.com.usptodatabasegenerator.uspto.dto.JobStatusDTO;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class EtlService {

    private final JobRunner jobRunner;

    private JobExecution etlJobExecution;

    public EtlService(JobRunner jobRunner) {
        this.jobRunner = jobRunner;
    }

    /* -------------------------------------------------------------------------------------------------------------- */

    public Boolean runExtractJob(JobEnum job) {

        Boolean result;

        if (jobRunner.checkJobAlreadyRunning(etlJobExecution)) {
            result = Boolean.FALSE;
        } else {
            etlJobExecution = null;
            etlJobExecution = jobRunner.run(job,
                    new JobParametersBuilder()
                            .addDate("timestamp", new Date())
                            .toJobParameters());
            result = (etlJobExecution != null) ? Boolean.TRUE : Boolean.FALSE;
        }
        return result;
    }

    /* -------------------------------------------------------------------------------------------------------------- */

    public JobStatusDTO getJobStatusDTO() {
        return jobRunner.getJobStatusDTO(etlJobExecution);
    }
}
