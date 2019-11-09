package labs.com.usptodatabasegenerator.uspto.domain.xml;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Inventor {

    @XStreamAsAttribute
    @XStreamAlias("sequence")
    private String sequence;

    @XStreamAsAttribute
    @XStreamAlias("designation")
    private String designation;

    @XStreamAlias("addressbook")
    private Addressbook addressbook;

}
