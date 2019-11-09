package labs.com.usptodatabasegenerator.uspto.dto;

import labs.com.usptodatabasegenerator.uspto.batch.JobEnum;
import labs.com.usptodatabasegenerator.uspto.batch.StepEnum;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;

import java.util.List;
import java.util.stream.Collectors;

public class JobStatusBuilder {
    
    public static JobStatusDTO build(JobExecution jobExecution){
        return JobStatusDTO
                .builder()
                .jobName(jobExecution.getJobInstance().getJobName())
                .status(jobExecution.getStatus().toString())
                .startTime(jobExecution.getStartTime())
                .endTime(jobExecution.getEndTime())
                .stepNumbers(JobEnum.stepNumberFromJobName(jobExecution.getJobInstance().getJobName()))
                .stepsStatus(buildStepsStatus(jobExecution))
                .build();
    }

    private static List<JobStatusDTO.StepStatusDTO> buildStepsStatus(JobExecution jobExecution) {

        return jobExecution.getStepExecutions()
                .stream()
                .map(JobStatusBuilder::buildStepStatus)
                .collect(Collectors.toList());
    }

    private static JobStatusDTO.StepStatusDTO buildStepStatus(StepExecution stepExecution) {

        String stepName = stepExecution.getStepName();

        return JobStatusDTO.StepStatusDTO
                .builder()
                .stepName(stepName)
                .startTime(stepExecution.getStartTime())
                .endTime(stepExecution.getEndTime())
                .status(stepExecution.getStatus().toString())
                .itemsTotalNumber(StepEnum.totalItemNumberFromStepName(stepName))
                .currentItem(StepEnum.currentItemFromStepName(stepName))
                .build();
    }

}
