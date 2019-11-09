package labs.com.usptodatabasegenerator.uspto.domain.xml;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import lombok.Data;

@Data
public class ClaimText {

    @XStreamAlias("claim-text")
    private String claimText;

}
