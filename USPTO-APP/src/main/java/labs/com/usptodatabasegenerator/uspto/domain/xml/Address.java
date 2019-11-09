package labs.com.usptodatabasegenerator.uspto.domain.xml;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Address {

    @XStreamAlias("city")
    private String city;

    @XStreamAlias("state")
    private String state;

    @XStreamAlias("country")
    private String country;
}
