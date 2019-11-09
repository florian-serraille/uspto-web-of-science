package labs.com.usptodatabasegenerator.uspto.domain.xml;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DocumentId {

    static Gson gson = new GsonBuilder().setPrettyPrinting().create();

    @XStreamAlias("country")
    private String country;
    @XStreamAlias("doc-number")
    private String documentNumber;
    @XStreamAlias("kind")
    private String kind;
    @XStreamAlias("name")
    private String name;
    @XStreamAlias("date")
    private String date;

    @Override
    public String toString() {
        return gson.toJson(this);
    }
}
