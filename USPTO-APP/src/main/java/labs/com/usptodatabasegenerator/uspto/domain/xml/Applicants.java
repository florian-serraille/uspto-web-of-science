package labs.com.usptodatabasegenerator.uspto.domain.xml;

import com.thoughtworks.xstream.annotations.XStreamImplicit;
import lombok.Data;

import java.util.List;

@Data
public class Applicants {

    @XStreamImplicit(itemFieldName="us-applicant")
    private List<Applicant> applicantList;

}
