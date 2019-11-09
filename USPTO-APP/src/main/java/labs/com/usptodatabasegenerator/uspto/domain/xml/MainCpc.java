package labs.com.usptodatabasegenerator.uspto.domain.xml;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import lombok.Data;

@Data
public class MainCpc {

    @XStreamAlias("classification-cpc")
    private CpcIpcr classificationCpcIpcr;
}
