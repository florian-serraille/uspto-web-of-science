package labs.com.usptodatabasegenerator.uspto.domain.xml;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Addressbook {

    @XStreamAlias("orgname")
    private String orgName;

    @XStreamAlias("last-name")
    private String lastName;

    @XStreamAlias("first-name")
    private String firstName;

    @XStreamAlias("address")
    private Address address;
}
