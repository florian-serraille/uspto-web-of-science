package labs.com.usptodatabasegenerator.uspto.domain.xml;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import lombok.Data;

@Data
public class Cpcs {

    @XStreamAlias("main-cpc")
    private MainCpc mainCpc;

    @XStreamAlias("further-cpc")
    private FurtherCpc furtherCpc;



}
