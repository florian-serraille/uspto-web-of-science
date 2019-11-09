package labs.com.usptodatabasegenerator.uspto.domain.xml;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import lombok.Data;

@Data
public class ClassificationNational {

    @XStreamAlias("country")
    private String country;

    @XStreamAlias("main-classification")
    private String mainClassification;

}
