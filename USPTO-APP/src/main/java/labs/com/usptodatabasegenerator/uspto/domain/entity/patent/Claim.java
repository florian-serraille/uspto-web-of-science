package labs.com.usptodatabasegenerator.uspto.domain.entity.patent;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "CLAIM")
public class Claim {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "patent_document_number")
    private UsptoPatent usptoPatent;

    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(name = "CLAIM")
    private String claim;


}
