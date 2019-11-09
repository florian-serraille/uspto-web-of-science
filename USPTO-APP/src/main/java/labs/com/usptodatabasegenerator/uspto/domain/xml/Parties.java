package labs.com.usptodatabasegenerator.uspto.domain.xml;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Parties {

    @XStreamAlias("us-applicants")
    private Applicants applicants;

    @XStreamAlias("inventors")
    private Inventors inventors;
}
