package labs.com.usptodatabasegenerator.uspto.domain.xml;

import com.thoughtworks.xstream.annotations.XStreamImplicit;
import lombok.Data;

import java.util.List;

@Data
public class FurtherCpc {

    @XStreamImplicit(itemFieldName="classification-cpc")
    private List<CpcIpcr> classificationCpcIpcrList;

}
