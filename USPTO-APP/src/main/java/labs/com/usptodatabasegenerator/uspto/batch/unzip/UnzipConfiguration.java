package labs.com.usptodatabasegenerator.uspto.batch.unzip;

import labs.com.usptodatabasegenerator.uspto.batch.JobConfiguration;
import labs.com.usptodatabasegenerator.uspto.batch.JobEnum;
import labs.com.usptodatabasegenerator.uspto.batch.StepEnum;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UnzipConfiguration extends JobConfiguration {


    public UnzipConfiguration(JobBuilderFactory jobBuilderFactory, StepBuilderFactory stepBuilderFactory) {
        super(jobBuilderFactory, stepBuilderFactory);
    }

    /* ------------------------------ JOB ------------------------------  */
    
    @Bean(name = "unzipJob")
    public Job unzipJob(Step unzipStep) {
        
        return jobBuilderFactory.get(JobEnum.UNZIP.getJobName())
                .start(unzipStep)
                .build();
    }

    /* ------------------------------ STEP ------------------------------  */

    @Bean
    public Step unzipStep(Tasklet unzipTasklet) {
        return stepBuilderFactory
                .get(StepEnum.UNZIP.getStepName())
                .tasklet(unzipTasklet)
                .build();
    }
    
}
