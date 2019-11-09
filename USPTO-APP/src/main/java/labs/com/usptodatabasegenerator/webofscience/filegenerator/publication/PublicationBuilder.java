package labs.com.usptodatabasegenerator.webofscience.filegenerator.publication;

import java.time.LocalDate;
import java.util.List;

public class PublicationBuilder {

    private String publicationType; 
    private String publicationName;
    private Integer yearPublished;
    private LocalDate publicationDate;
    private List<String> citedReferences;
    private String inventors;
    private String usptoClassification;
    private String internationalClassification;
    private String patentIdentification;
    private String abstractText;
    private String patentTitle;

    public Publication build() {
        return new Publication(publicationType,
                publicationName,
                yearPublished,
                publicationDate,
                citedReferences,
                inventors,
                usptoClassification,
                internationalClassification,
                patentIdentification,
                abstractText,
                patentTitle);
    }

    public PublicationBuilder addPublicationType(String publicationType) {
        this.publicationType = publicationType;
    return this;
    }

    public PublicationBuilder addPublicationName(String publicationName) {
        this.publicationName = publicationName;
        return this;
    }

    public PublicationBuilder addYearPublished(Integer yearPublished) {
        this.yearPublished = yearPublished;
        return this;
    }

    public PublicationBuilder addPublicationDate(LocalDate publicationDate) {
        this.publicationDate = publicationDate;
        return this;
    }

    public PublicationBuilder addCitedReferences(List<String> citedReferences) {
        if (this.citedReferences != null && !this.citedReferences.isEmpty()){
            citedReferences.addAll(citedReferences);
        } else {
            this.citedReferences = citedReferences;
        }
        return this;
    }

    public PublicationBuilder addInventor(String inventors) {
        this.inventors = inventors;
        return this;
    }

    public PublicationBuilder addUsptoClassification(String usptoClassification) {
        this.usptoClassification = usptoClassification;
        return this;
    }

    public PublicationBuilder addInternationalClassification(String internationalClassification) {
        this.internationalClassification = internationalClassification;
        return this;
    }

    public PublicationBuilder addPatentIdentification(String patentIdentification) {
        this.patentIdentification = patentIdentification;
        return this;
    }

    public PublicationBuilder addAbstractText(String abstractText) {
        if (this.abstractText != null){
            this.abstractText =  abstractText + " " + this.abstractText;   
        } else {
            this.abstractText = abstractText;
        }
        return this;
    }

    public PublicationBuilder addPatentTitle(String patentTitle) {
        this.patentTitle = patentTitle;
        return this;
    }
}
