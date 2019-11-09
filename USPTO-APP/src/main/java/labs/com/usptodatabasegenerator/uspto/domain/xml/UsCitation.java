package labs.com.usptodatabasegenerator.uspto.domain.xml;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import lombok.Data;

@Data
public class UsCitation {

    @XStreamAlias("patcit")
    private Patcit patcit;

    @XStreamAlias("nplcit")
    private Nplcit nplcit;

    @XStreamAlias("category")
    private String category;

    @XStreamAlias("classification-cpc-text")
    private String classificationCpcText;

    @XStreamAlias("classification-national")
    private ClassificationNational classificatioNnational;
}
