package labs.com.usptodatabasegenerator.uspto.batch.download;

import labs.com.usptodatabasegenerator.uspto.batch.JobConfiguration;
import labs.com.usptodatabasegenerator.uspto.batch.JobEnum;
import labs.com.usptodatabasegenerator.uspto.batch.StepEnum;
import labs.com.usptodatabasegenerator.uspto.domain.entity.file.UsptoFile;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;

@Configuration
public class DownloadJobConfiguration extends JobConfiguration {

    protected DownloadJobConfiguration(JobBuilderFactory jobBuilderFactory, StepBuilderFactory stepBuilderFactory) {
        super(jobBuilderFactory, stepBuilderFactory);
    }

    @Bean(name = "downloadJob")
    public Job downloadJob(Step downloadStep){
        return jobBuilderFactory
                .get(JobEnum.DOWNLOAD.getJobName())
                .start(downloadStep)
                .build();
    }

    @Bean
    public Step downloadStep(ItemReader<LocalDate> downloadReader,
                             ItemProcessor<LocalDate, UsptoFile> downloadProcessor,
                             ItemWriter<UsptoFile> downloadWriter) {
        return stepBuilderFactory
                .get(StepEnum.DOWNLOAD.getStepName())
                .<LocalDate, UsptoFile>chunk(1)
                .reader(downloadReader)
                .processor(downloadProcessor)
                .writer(downloadWriter)
                .build();
    }

}
