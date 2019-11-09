package labs.com.usptodatabasegenerator.uspto.batch;

import labs.com.usptodatabasegenerator.uspto.dto.JobStatusBuilder;
import labs.com.usptodatabasegenerator.uspto.dto.JobStatusDTO;
import labs.com.usptodatabasegenerator.uspto.util.SpringContextUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class JobRunner {

    private static final Logger LOG = LoggerFactory.getLogger(JobRunner.class);

    private final JobOperator jobOperator;
    private final JobExplorer jobExplorer;
    private final JobLauncher jobLauncher;

    public JobRunner(@Qualifier("simpleJobOperator") JobOperator jobOperator,
                     @Qualifier("asyncLauncher") JobLauncher jobLauncher,
                     JobExplorer jobExplorer) {
        this.jobOperator = jobOperator;
        this.jobExplorer = jobExplorer;
        this.jobLauncher = jobLauncher;
    }

    /* -------------------------------------------------------------------------------------------------------------- */

    public JobStatusDTO getJobStatusDTO(JobExecution jobExecution) {
        JobStatusDTO jobStatusDTO = null;
        if (jobExecution != null) {
            jobStatusDTO = JobStatusBuilder.build(jobExecution);
        }
        return jobStatusDTO;
    }

    /* -------------------------------------------------------------------------------------------------------------- */

    public JobExecution run(JobEnum jobName, JobParameters jobParameters) {

        JobExecution jobExecution;

        try {
            Job job = SpringContextUtil.getBean(jobName.getJobName(), Job.class);
            logLaunchJob(jobName);
            jobExecution = jobLauncher.run(job, jobParameters);
            LOG.info("Launched...");
        } catch (Exception e) {
            jobExecution = null;
        }

        return jobExecution;
    }

    /* -------------------------------------------------------------------------------------------------------------- */

    public Boolean checkJobAlreadyRunning(JobExecution jobExecution) {

        Boolean result = Boolean.FALSE;

        if (jobExecution != null) {
            BatchStatus jobStatus = jobExecution.getStatus();

            if (BatchStatus.STARTED.equals(jobStatus) ||
                    BatchStatus.STARTING.equals(jobStatus) ||
                    BatchStatus.STOPPING.equals(jobStatus)) {

                result = Boolean.TRUE;
                LOG.warn("Job already running " + jobExecution.getJobConfigurationName());
            }
        }
        return result;
    }

    /* -------------------------------------------------------------------------------------------------------------- */

    public boolean stop(JobExecution jobExecution) {

        Boolean stoped = Boolean.FALSE;

        if (jobExecution != null) {
            try {
                jobOperator.stop(jobExecution.getJobId());
                stoped = Boolean.TRUE;
                LOG.warn("Stopping job " + jobExecution.getJobConfigurationName());

            } catch (Exception ignored) {
                LOG.warn("Unable to stop job " + jobExecution.getJobConfigurationName());
            }
        }
        return stoped;
    }

    /* -------------------------------------------------------------------------------------------------------------- */

    private void logLaunchJob(JobEnum jobName) {
        LOG.info("");
        LOG.info("==============================");
        LOG.info("LAUNCHING JOB: " + jobName.getJobName());
        LOG.info("==============================");
    }

    private BatchStatus getJobStatus(Long id) {
        JobExecution jobExecution = jobExplorer.getJobExecution(id);
        return (jobExecution != null) ? jobExecution.getStatus() : BatchStatus.UNKNOWN;
    }
}
