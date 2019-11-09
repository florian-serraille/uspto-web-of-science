package labs.com.usptodatabasegenerator.uspto.domain.xml;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BibliographicDataGrant {

    private static Gson gson = new GsonBuilder().setPrettyPrinting().create();

    /* Parameters*/
    @XStreamAsAttribute
    @XStreamAlias("id")
    private String id;

    @XStreamAsAttribute
    @XStreamAlias("lang")
    private String lang;

    @XStreamAsAttribute
    @XStreamAlias("status")
    private String status;

    @XStreamAsAttribute
    @XStreamAlias("country")
    private String country;

    /* Elements */
    @XStreamAlias("publication-reference")
    private PublicationReference publicationReference;

    @XStreamAlias("application-reference")
    private AplicationReference aplicationReference;

    @XStreamAlias("us-application-series-code")
    private String applicationSeriesCode;

    @XStreamAlias("classifications-cpc")
    private Cpcs classificationsCpc;

    @XStreamAlias("classifications-ipcr")
    private Ipcrs classificationsIpcr;

    @XStreamAlias("us-parties")
    private Parties parties;

    @XStreamAlias("invention-title")
    private String inventionTitle;

    @XStreamAlias("us-references-cited")
    private UsReferences usReferences;

    @XStreamAlias("number-of-claims")
    private String numberOfClaims;

    @XStreamAlias("assignees")
    private Assignees assignees;

    @Override
    public String toString() {
        return gson.toJson(this);
    }
}
