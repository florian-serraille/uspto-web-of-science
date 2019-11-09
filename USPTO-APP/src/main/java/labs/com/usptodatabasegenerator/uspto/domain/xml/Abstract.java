package labs.com.usptodatabasegenerator.uspto.domain.xml;

import com.thoughtworks.xstream.annotations.XStreamImplicit;
import lombok.Data;

import java.util.List;

@Data
public class Abstract {

    @XStreamImplicit(itemFieldName="p")
    private List<String> abstracts;
}
