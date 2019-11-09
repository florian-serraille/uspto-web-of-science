package labs.com.usptodatabasegenerator.uspto.domain.xml;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import lombok.Data;

@Data
public class DateContainer {

    @XStreamAlias("date")
    private String date;
}
