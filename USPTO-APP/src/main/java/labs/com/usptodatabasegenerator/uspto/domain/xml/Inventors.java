package labs.com.usptodatabasegenerator.uspto.domain.xml;

import com.thoughtworks.xstream.annotations.XStreamImplicit;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class Inventors {

    @XStreamImplicit(itemFieldName="inventor")
    private List<Inventor> inventorList;

}
