package labs.com.usptodatabasegenerator.uspto.domain.entity.patent;

import labs.com.usptodatabasegenerator.uspto.domain.entity.enums.ClassificationEnum;
import lombok.*;

import javax.persistence.*;

/**
 * https://www.cooperativepatentclassification.org/index.html
 */

@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Entity
@Table(name = "INTERNATIONAL_CLASSIFICATION")
public class InternationalClassification {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Getter
    @Column(name = "TYPE")
    @Enumerated(EnumType.STRING)
    private ClassificationEnum type;
    @Column(name = "VERSION_INDICATOR")
    private String versionIndicator;
    @Column(name = "SECTION")
    private String section;
    @Column(name = "CLASS_NAME")
    private String className;
    @Column(name = "SUB_CLASS")
    private String subClass;
    @Column(name = "MAIN_GROUP")
    private String mainGroup;
    @Column(name = "SUB_GROUP")
    private String subGroup;

    public String toWebOfScience(){
        return section + className + subClass + " " + mainGroup + "/" + subGroup + " (" + versionIndicator + ")";
    }
    
}
