package labs.com.usptodatabasegenerator.uspto.domain.xml;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@XStreamAlias("us-patent-grant")
public class Patent {

    static Gson gson = new GsonBuilder().setPrettyPrinting().create();

    /* Parameters*/

    @XStreamAsAttribute
    @XStreamAlias("lang")
    private String language;

    @XStreamAsAttribute
    @XStreamAlias("dtd-version")
    private String dtdVersion;

    @XStreamAsAttribute
    @XStreamAlias("file")
    private String file;

    @XStreamAsAttribute
    @XStreamAlias("status")
    private String status;

    @XStreamAsAttribute
    @XStreamAlias("id")
    private String id;

    @XStreamAsAttribute
    @XStreamAlias("country")
    private String country;

    @XStreamAsAttribute
    @XStreamAlias("date-produced")
    private String producedDate;

    @XStreamAsAttribute
    @XStreamAlias("date-publ")
    private String publicationDate;

    /* Elements */
    @XStreamAlias("us-bibliographic-data-grant")
    private BibliographicDataGrant bibliographicDataGrant;

    @XStreamAlias("us-claim-statement")
    private String ClaimStatement;

    @XStreamAlias("claims")
    private Claims claims;

    @XStreamAlias("abstract")
    private Abstract patentAbstract;
    
    @Override
    public String toString() {
        return gson.toJson(this);
    }
}
