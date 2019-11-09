package labs.com.usptodatabasegenerator.uspto.domain.xml;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import lombok.Data;

@Data
public class Applicant {

    @XStreamAsAttribute
    @XStreamAlias("sequence")
    private String sequence;

    @XStreamAsAttribute
    @XStreamAlias("app-type")
    private String appType;

    @XStreamAsAttribute
    @XStreamAlias("designation")
    private String designation;

    @XStreamAsAttribute
    @XStreamAlias("applicant-authority-category")
    private String applicantAuthorityCategory;

    @XStreamAlias("addressbook")
    private Addressbook addressbook;

    @XStreamAlias("residence")
    private Residence residence;


}
