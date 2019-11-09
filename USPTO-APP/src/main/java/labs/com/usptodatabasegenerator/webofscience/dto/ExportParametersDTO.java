package labs.com.usptodatabasegenerator.webofscience.dto;

import labs.com.usptodatabasegenerator.uspto.domain.entity.enums.ContentTypeEnum;
import labs.com.usptodatabasegenerator.uspto.domain.entity.enums.PersonTypeEnum;
import labs.com.usptodatabasegenerator.uspto.domain.entity.enums.ReferenceEnum;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class ExportParametersDTO {

    private String fileName;
    private PersonTypeEnum personType;
    private ContentTypeEnum content;
    private ReferenceEnum reference;
}
