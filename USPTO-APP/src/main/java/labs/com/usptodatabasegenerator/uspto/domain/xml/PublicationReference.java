package labs.com.usptodatabasegenerator.uspto.domain.xml;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PublicationReference {

    static Gson gson = new GsonBuilder().setPrettyPrinting().create();

    @XStreamAlias("document-id")
    private DocumentId documentId;

    @Override
    public String toString() {
        return gson.toJson(this);
    }
}
