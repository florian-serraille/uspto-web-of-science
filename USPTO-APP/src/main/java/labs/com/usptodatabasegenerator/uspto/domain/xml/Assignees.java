package labs.com.usptodatabasegenerator.uspto.domain.xml;

import com.thoughtworks.xstream.annotations.XStreamImplicit;
import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
public class Assignees {

    @XStreamImplicit(itemFieldName="assignee")
    private List<Assignee> assigneeList = new ArrayList<>();

}
