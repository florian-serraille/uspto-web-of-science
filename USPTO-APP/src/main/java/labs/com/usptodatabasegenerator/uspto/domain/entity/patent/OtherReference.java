package labs.com.usptodatabasegenerator.uspto.domain.entity.patent;

import lombok.*;

import javax.persistence.*;

@ToString
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "OTHER_REFERENCE")
public class OtherReference {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;
    @ManyToOne
    @JoinColumn(name = "PATENT_DOCUMENT_NUMBER")
    private UsptoPatent usptoPatent;
    @Column(name = "DESCRIPTION")
    private String description;


    public String toWebOfScience() {
        return this.toString();
    }
}
