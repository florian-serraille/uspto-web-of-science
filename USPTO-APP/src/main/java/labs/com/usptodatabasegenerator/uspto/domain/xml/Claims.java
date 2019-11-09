package labs.com.usptodatabasegenerator.uspto.domain.xml;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import lombok.Data;

import java.util.List;

@Data
public class Claims {

    /* Parameters*/

    @XStreamAsAttribute
    @XStreamAlias("id")
    private String id;

    /* Elements */
    @XStreamImplicit(itemFieldName="claim")
    private List<Claim> claimList;


}
