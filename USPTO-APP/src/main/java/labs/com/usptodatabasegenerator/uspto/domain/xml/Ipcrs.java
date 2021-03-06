package labs.com.usptodatabasegenerator.uspto.domain.xml;

import com.thoughtworks.xstream.annotations.XStreamImplicit;
import lombok.Data;

import java.util.List;

@Data
public class Ipcrs {

    @XStreamImplicit(itemFieldName="classification-ipcr")
    private List<CpcIpcr> ipcrList;

}
