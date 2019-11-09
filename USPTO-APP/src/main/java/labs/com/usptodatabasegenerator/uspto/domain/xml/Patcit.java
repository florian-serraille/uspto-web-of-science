package labs.com.usptodatabasegenerator.uspto.domain.xml;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import lombok.Data;

@Data
public class Patcit {

    @XStreamAsAttribute
    @XStreamAlias("num")
    private String num;

    @XStreamAlias("document-id")
    private DocumentId documentId;
}
