package labs.com.usptodatabasegenerator.uspto.batch;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public abstract class JobConfiguration {
    protected final JobBuilderFactory jobBuilderFactory;
    protected final StepBuilderFactory stepBuilderFactory;

    protected JobConfiguration(JobBuilderFactory jobBuilderFactory, StepBuilderFactory stepBuilderFactory) {
        this.jobBuilderFactory = jobBuilderFactory;
        this.stepBuilderFactory = stepBuilderFactory;
    }


    @Bean(name = "fullJob")
    public Job fullJob(Step unzipStep, Step cleanStep, Step loadStep) {

        return jobBuilderFactory
                .get(JobEnum.UNZIP_CLEAN_LOAD.getJobName())
                .start(unzipStep)
                .next(cleanStep)
                .next(loadStep)
                .build();
    }
}
