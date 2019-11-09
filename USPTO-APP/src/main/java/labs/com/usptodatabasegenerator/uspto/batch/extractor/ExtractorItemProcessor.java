package labs.com.usptodatabasegenerator.uspto.batch.extractor;

import labs.com.usptodatabasegenerator.uspto.domain.entity.enums.ClassificationEnum;
import labs.com.usptodatabasegenerator.uspto.domain.entity.enums.ContentTypeEnum;
import labs.com.usptodatabasegenerator.uspto.domain.entity.enums.PersonTypeEnum;
import labs.com.usptodatabasegenerator.uspto.domain.entity.enums.ReferenceEnum;
import labs.com.usptodatabasegenerator.uspto.domain.entity.patent.*;
import labs.com.usptodatabasegenerator.uspto.domain.service.impl.ClassificationServiceImpl;
import labs.com.usptodatabasegenerator.uspto.domain.service.impl.OtherReferenceServiceImpl;
import labs.com.usptodatabasegenerator.uspto.domain.service.impl.PatentReferenceServiceImpl;
import labs.com.usptodatabasegenerator.uspto.domain.service.impl.PersonServiceImpl;
import labs.com.usptodatabasegenerator.webofscience.filegenerator.publication.Publication;
import labs.com.usptodatabasegenerator.webofscience.filegenerator.publication.PublicationBuilder;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@JobScope
@Component
public class ExtractorItemProcessor implements ItemProcessor<UsptoPatent, Publication> {

    public static long currentItem;
    private final PersonTypeEnum personType;
    private final ContentTypeEnum contentType;
    private final ReferenceEnum referenceType;

    public ExtractorItemProcessor(@Value("#{jobParameters['personType']}") String personTypeParam,
                                  @Value("#{jobParameters['contentType']}") String contentTypeParam,
                                  @Value("#{jobParameters['referenceType']}") String referenceParam) {
        this.personType = PersonTypeEnum.valueOf(personTypeParam);
        this.contentType = ContentTypeEnum.valueOf(contentTypeParam);
        this.referenceType = ReferenceEnum.valueOf(referenceParam);
        currentItem = 0L;
    }

    @Override
    public Publication process(UsptoPatent item) {

        currentItem++;

        PublicationBuilder publicationBuilder = new PublicationBuilder()
                .addPublicationType("J")
                .addPublicationName("USPTO_Patent_Grant")
                .addYearPublished(item.getPublicationDate().getYear())
                .addPublicationDate(item.getPublicationDate())
                .addCitedReferences(item.getPatentReferences()
                        .stream()
                        .filter(r -> PatentReferenceServiceImpl.filterByType(r, referenceType))
                        .map(PatentReference::toWebOfScience)
                        .sorted()
                        .collect(Collectors.toList()))
                .addCitedReferences(item.getOtherReferences()
                        .stream()
                        .filter(r -> OtherReferenceServiceImpl.filterByType( referenceType))
                        .map(OtherReference::toWebOfScience)
                        .sorted()
                        .collect(Collectors.toList()))
                .addInventor(item.getInventors()
                        .stream()
                        .filter(p -> PersonServiceImpl.filterByType(p, personType))
                        .map(Person::toWebOfScience)
                        .distinct()
                        .collect(Collectors.joining("; ")))
                .addInternationalClassification(item.getInternationalClassifications()
                        .stream()
                        .filter(c -> ClassificationServiceImpl.filterByType(c, ClassificationEnum.CPC))
                        .map(InternationalClassification::toWebOfScience)
                        .sorted()
                        .collect(Collectors.joining("; ")))
                .addPatentIdentification(item.toWebOfScience())
                .addAbstractText(item.getClaims()
                        .stream()
                        .filter(claim ->
                                (contentType.equals(ContentTypeEnum.ALL)) ||
                                        (contentType.equals(ContentTypeEnum.CLAIMS)))
                        .map(Claim::getClaim)
                        .collect(Collectors.joining()))
                .addPatentTitle(item.getTitle());

        if (contentType.equals(ContentTypeEnum.ALL) || contentType.equals(ContentTypeEnum.ABSTRACT)){
            publicationBuilder.addAbstractText(item.getPatentAbstract());
        }

        return publicationBuilder.build();

    }
}
