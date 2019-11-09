package labs.com.usptodatabasegenerator.uspto.domain.xml;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import lombok.Data;

@Data
public class Assignee {

    @XStreamAlias("addressbook")
    private Addressbook addressbook;

}
