package labs.com.usptodatabasegenerator.uspto.domain.xml;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

public class AplicationReference {

    @XStreamAsAttribute
    @XStreamAlias("appl-type")
    private String applType;

    @XStreamAlias("document-id")
    private DocumentId documentId;

}
