package labs.com.usptodatabasegenerator.uspto.batch.load;

import labs.com.usptodatabasegenerator.uspto.batch.JobConfiguration;
import labs.com.usptodatabasegenerator.uspto.batch.JobEnum;
import labs.com.usptodatabasegenerator.uspto.batch.StepEnum;
import labs.com.usptodatabasegenerator.uspto.domain.entity.patent.UsptoPatent;
import labs.com.usptodatabasegenerator.uspto.domain.xml.*;
import labs.com.usptodatabasegenerator.uspto.util.SpringContextUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.MultiResourceItemReader;
import org.springframework.batch.item.file.builder.MultiResourceItemReaderBuilder;
import org.springframework.batch.item.xml.StaxEventItemReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.oxm.Unmarshaller;
import org.springframework.oxm.xstream.XStreamMarshaller;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Objects;

@Configuration
public class LoaderConfiguration extends JobConfiguration {

    public static Long totalItemNumber;
    private final Path cleanPathDirectory;

    private final Logger LOG;
    private final Integer CHUNK_SIZE;

    public LoaderConfiguration(@Value(value = "${application.chunk-size}") Integer chunkSize,
                               @Value("${directory.clean}") String cleanDirectory,
                               JobBuilderFactory jobBuilderFactory,
                               StepBuilderFactory stepBuilderFactory) {

        super(jobBuilderFactory, stepBuilderFactory);

        this.LOG = LoggerFactory.getLogger(this.getClass());
        this.CHUNK_SIZE = chunkSize;
        
        this.cleanPathDirectory = Paths.get(cleanDirectory);
    }

    @Bean(name = "loadJob")
    public Job loadJob(Step loadStep) {

        return jobBuilderFactory
                .get(JobEnum.LOAD.getJobName())
                .start(loadStep)
                .build();
    }

    @Bean
    public Step loadStep(ItemProcessor<Patent, UsptoPatent> loaderItemProcessor, ItemWriter<UsptoPatent> loaderItemPWriter) throws IOException {
        return stepBuilderFactory.get(StepEnum.LOAD.getStepName())
                .<Patent, UsptoPatent>chunk(CHUNK_SIZE)
                .reader(patentMultiResourceReader())
                .processor(loaderItemProcessor)
                .writer(loaderItemPWriter)
                .build();
    }

    @Bean
    public StaxEventItemReader<Patent> patentItemReader() {
        StaxEventItemReader<Patent> patentStaxEventItemReader = new StaxEventItemReader<>();
        patentStaxEventItemReader.setFragmentRootElementName("us-patent-grant");
        patentStaxEventItemReader.setUnmarshaller(patentUnmarshaller());
        patentStaxEventItemReader.setStrict(Boolean.FALSE);
        try {
            patentStaxEventItemReader.afterPropertiesSet();
        } catch (Exception e) {
            throw new RuntimeException("Unable to configure patent item reader");
        }
        return patentStaxEventItemReader;
    }

    @Bean
    @StepScope
    public MultiResourceItemReader<Patent> patentMultiResourceReader() throws IOException {
        return new MultiResourceItemReaderBuilder<Patent>()
                .name("patenteMultiResourcesReader")
                .resources(getResources(cleanPathDirectory))
                .delegate(patentItemReader())
                .build();
    }

    private Unmarshaller patentUnmarshaller() {
        XStreamMarshaller marshaller = new XStreamMarshaller();
        marshaller.setAnnotatedClasses(
                Abstract.class,
                Addressbook.class,
                Address.class,
                AplicationReference.class,
                Applicant.class,
                Applicants.class,
                Assignee.class,
                Assignees.class,
                BibliographicDataGrant.class,
                Claim.class,
                Claims.class,
                ClaimText.class,
                ClassificationNational.class,
                CpcIpcr.class,
                Cpcs.class,
                DateContainer.class,
                DocumentId.class,
                FurtherCpc.class,
                GeneratingOffice.class,
                Inventor.class,
                Inventors.class,
                Ipcrs.class,
                MainCpc.class,
                Nplcit.class,
                Parties.class,
                Patcit.class,
                Patent.class,
                PublicationReference.class,
                Residence.class,
                UsCitation.class,
                UsReferences.class
        );
        marshaller.getXStream().ignoreUnknownElements();
        return marshaller;
    }

    private Resource[] getResources(Path path) throws IOException {
        Resource[] resources = Arrays.stream(SpringContextUtil.getResources("file:" + path + "/*"))
                .filter(Resource::exists)
                .filter(resource -> Objects.requireNonNull(resource.getFilename()).toLowerCase().endsWith(".xml"))
                .toArray(Resource[]::new);
        
        totalItemNumber = (long) resources.length;
        LOG.info("Found " + totalItemNumber + " patents to load");
        
        return resources;
    }
    
}
