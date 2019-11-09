package labs.com.usptodatabasegenerator.webofscience.filegenerator.publication;

import labs.com.usptodatabasegenerator.webofscience.filegenerator.tag.Tag;
import labs.com.usptodatabasegenerator.webofscience.filegenerator.tag.Tags;

import java.time.LocalDate;
import java.util.List;

public class Publication {

    @Tag(Tags.PT)
    private String publicationType;

    @Tag(Tags.SO)
    private String publicationName;
    
    @Tag(Tags.PY)
    private Integer yearPublished;

    @Tag(Tags.DA)
    private LocalDate publicationDate;
    
    @Tag(Tags.CR)
    private List<String> references;

    @Tag(Tags.AU)
    private String inventors;
    
    @Tag(Tags.DE)
    private String usptoClassification;

    @Tag(Tags.ID)
    private String internationalClassification;

    @Tag(Tags.DI)
    private String patentIdentification;

    @Tag(Tags.AB)
    private String abstractText;

    @Tag(Tags.TI)
    private String patentTitle;

    Publication(String publicationType, 
                String publicationName,
                Integer yearPublished,
                LocalDate publicationDate,
                List<String> references,
                String inventors,
                String usptoClassification,
                String internationalClassification,
                String patentIdentification,
                String abstractText,
                String patentTitle) {
        
        this.publicationType = publicationType;
        this.publicationName = publicationName;
        this.yearPublished = yearPublished;
        this.publicationDate = publicationDate;
        this.references = references;
        this.inventors = inventors;
        this.usptoClassification = usptoClassification;
        this.internationalClassification = internationalClassification;
        this.patentIdentification = patentIdentification;
        this.abstractText = abstractText;
        this.patentTitle = patentTitle;
    }

    @Override
    public String toString() {
        return PublicationHelper.PublicationToString(this);
    }
}
