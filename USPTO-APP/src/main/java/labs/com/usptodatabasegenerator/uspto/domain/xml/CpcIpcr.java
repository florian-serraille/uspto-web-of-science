package labs.com.usptodatabasegenerator.uspto.domain.xml;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import lombok.Data;

@Data
public class CpcIpcr {

    @XStreamAlias("cpc-version-indicator")
    private DateContainer cpcVersionIndicator;

    @XStreamAlias("ipc-version-indicator")
    private DateContainer ipcVersionIndicator;

    @XStreamAlias("section")
    private String section;

    @XStreamAlias("class")
    private String className;

    @XStreamAlias("subclass")
    private String subClass;

    @XStreamAlias("main-group")
    private String mainGroup;

    @XStreamAlias("subgroup")
    private String subGroup;

    @XStreamAlias("symbol-position")
    private String symbolPosition;

    @XStreamAlias("classification-value")
    private String classificationValue;

    @XStreamAlias("action-date")
    private DateContainer actionDate;

    @XStreamAlias("generating-office")
    private GeneratingOffice generatingOffice;

    @XStreamAlias("classification-status")
    private String classificationStatus;

    @XStreamAlias("classification-data-source")
    private String classificationDataSource;

    @XStreamAlias("scheme-origination-code")
    private String schemeOriginationCode;
}
