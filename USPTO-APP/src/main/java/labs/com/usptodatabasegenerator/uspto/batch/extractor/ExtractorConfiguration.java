package labs.com.usptodatabasegenerator.uspto.batch.extractor;

import labs.com.usptodatabasegenerator.uspto.batch.JobConfiguration;
import labs.com.usptodatabasegenerator.uspto.batch.JobEnum;
import labs.com.usptodatabasegenerator.uspto.batch.StepEnum;
import labs.com.usptodatabasegenerator.uspto.domain.entity.patent.UsptoPatent;
import labs.com.usptodatabasegenerator.uspto.domain.service.UsptoPatentService;
import labs.com.usptodatabasegenerator.webofscience.filegenerator.publication.Publication;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.builder.FlatFileItemWriterBuilder;
import org.springframework.batch.item.file.transform.PassThroughLineAggregator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;

import javax.persistence.EntityManagerFactory;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class ExtractorConfiguration extends JobConfiguration {

    protected ExtractorConfiguration(JobBuilderFactory jobBuilderFactory, StepBuilderFactory stepBuilderFactory) {
        super(jobBuilderFactory, stepBuilderFactory);
    }

    @Bean
    public Job extractorJob(Step extractorStep) {
        return jobBuilderFactory
                .get(JobEnum.EXTRACTOR.getJobName())
                .start(extractorStep)
                .build();
    }

    @Bean
    public Step extractorStep(@Value(value = "${application.chunk-size}") Integer chunkSize,
                              ItemReader<UsptoPatent> extractorReader,
                              ItemProcessor<UsptoPatent, Publication> extractorProcessor,
                              ItemWriter<Publication> extractorWriter) {
        return stepBuilderFactory
                .get(StepEnum.EXTRACTOR.getStepName())
                .<UsptoPatent, Publication>chunk(chunkSize)
                .reader(extractorReader)
                .processor(extractorProcessor)
                .writer(extractorWriter)
                .build();
    }

    @Bean
    @JobScope
    public JpaPagingItemReader extractorReader(@Value(value = "${application.chunk-size}") Integer chunkSize,
                                               EntityManagerFactory entityManagerFactory,
                                               UsptoPatentService usptoPatentService) {

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("idList", usptoPatentService.getIdList());

        return new JpaPagingItemReaderBuilder<UsptoPatent>()
                .name("extractorReader")
                .entityManagerFactory(entityManagerFactory)
                .queryString("select a from UsptoPatent a where a.id in :idList")
                .parameterValues(parameters)
                .pageSize(chunkSize)
                .build();
    }

    @Bean
    @JobScope
    public FlatFileItemWriter<Publication> extractorWriter(@Value("#{jobParameters['fileName']}") final String fileName,
                                                           @Value("${directory.export}") String exportDirectory) {
        return new FlatFileItemWriterBuilder<Publication>()
                .name("extractorWriter")
                .resource(new FileSystemResource(Paths.get(exportDirectory, fileName)))
                .lineAggregator(new PassThroughLineAggregator<>())
                .build();
    }
}
