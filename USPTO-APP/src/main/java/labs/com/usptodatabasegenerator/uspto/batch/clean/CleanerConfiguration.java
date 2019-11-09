package labs.com.usptodatabasegenerator.uspto.batch.clean;

import labs.com.usptodatabasegenerator.uspto.batch.JobConfiguration;
import labs.com.usptodatabasegenerator.uspto.batch.JobEnum;
import labs.com.usptodatabasegenerator.uspto.batch.StepEnum;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Configuration
public class CleanerConfiguration extends JobConfiguration {

    public static long totalItemNumber;

    protected CleanerConfiguration(JobBuilderFactory jobBuilderFactory, StepBuilderFactory stepBuilderFactory) {
        super(jobBuilderFactory, stepBuilderFactory);
        totalItemNumber = 0L;
    }

    @Bean
    public Job cleanerJob(Step cleanStep) {
        return jobBuilderFactory
                .get(JobEnum.CLEANER.getJobName())
                .start(cleanStep)
                .build();
    }
    
    @Bean
    @JobScope
    public Step cleanStep(@Value("${directory.unzip}") String unzipDirectory, Tasklet cleanerTasklet) throws IOException {

        totalItemNumber = Files.list(Paths.get(unzipDirectory )).count();
        
        return stepBuilderFactory.get(StepEnum.CLEAN.getStepName())
                .tasklet(cleanerTasklet)
                .taskExecutor(new SimpleAsyncTaskExecutor())
                .build();
    }
}
