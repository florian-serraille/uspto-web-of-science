package labs.com.usptodatabasegenerator.uspto.domain.xml;

import com.thoughtworks.xstream.annotations.XStreamImplicit;
import lombok.Data;

import java.util.List;

@Data
public class UsReferences {

    /* Elements */
    @XStreamImplicit(itemFieldName="us-citation")
    private List<UsCitation> usCitationList;

}
