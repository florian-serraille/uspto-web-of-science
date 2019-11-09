package labs.com.usptodatabasegenerator.uspto.domain.entity.patent;


import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@ToString(onlyExplicitlyIncluded = true)
@Table(name = "PATENT")
public class UsptoPatent {

    @Id
    @Column(name = "DOCUMENT_NUMBER")
    private String documentNumber;

    @NotNull
    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(name = "PATENT_PEOPLE", joinColumns = @JoinColumn(name = "PATENT_DOCUMENT_NUMBER"),
            inverseJoinColumns = @JoinColumn(name = "PERSON_ID"))
    private Set<Person> inventors = new HashSet<>();

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "PATENT_PEOPLE", joinColumns = @JoinColumn(name = "PATENT_DOCUMENT_NUMBER"),
            inverseJoinColumns = @JoinColumn(name = "PERSON_ID"))
    private Set<Person> applicants;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "PATENT_PEOPLE", joinColumns = @JoinColumn(name = "PATENT_DOCUMENT_NUMBER"),
            inverseJoinColumns = @JoinColumn(name = "PERSON_ID"))
    private Set<Person> assignees;


    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "PATENT_INTERNATIONAL_CLASSIFICATION", joinColumns = @JoinColumn(name = "PATENT_DOCUMENT_NUMBER"),
            inverseJoinColumns = @JoinColumn(name = "INTERNATIONAL_CLASSIFICATION_ID"))
    private Set<InternationalClassification> internationalClassifications;

    @OneToMany(mappedBy = "usptoPatent", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Claim> claims;

    @OneToMany(mappedBy = "usptoPatent", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PatentReference> patentReferences;

    @OneToMany(mappedBy = "usptoPatent", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OtherReference> otherReferences;

    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(name = "ABSTRACT")
    private String patentAbstract;

    @Column(name = "PUBLICATION_DATE")
    private LocalDate publicationDate;
    @ToString.Include
    @Column(name = "TITLE")
    private String title;
    @Column(name = "FILE_NAME")
    private String fileName;

    public String toWebOfScience() {
        return "US" + getDocumentNumber();
    }

}
