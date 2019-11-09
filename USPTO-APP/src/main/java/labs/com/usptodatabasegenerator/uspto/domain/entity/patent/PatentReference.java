package labs.com.usptodatabasegenerator.uspto.domain.entity.patent;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "PATENT_REFERENCE")
public class PatentReference {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "PATENT_DOCUMENT_NUMBER")
    private UsptoPatent usptoPatent;
    @NotNull
    @Column(name = "COUNTRY")
    @Basic(optional = false)
    private String country;
    @NotNull
    @Basic(optional = false)
    @Column(name = "REFERENCE_DOCUMENT_NUMBER")
    private String docNumber;
    @NotNull
    @Column(name = "PUBLICATION_DATE")
    @Basic(optional = false)
    private LocalDate publicationDate;
    @Column(name = "KIND")
    private String kind;
    @Column(name = "TITLE")
    private String title;


    public String toWebOfScience() {
        StringBuilder builder = new StringBuilder();
        builder.append(docNumber);
        builder.append(" ");
        builder.append(publicationDate);
        builder.append(" ");
        if (title !=null){
            builder.append(title);
        }

        return builder.toString();
    }
}
