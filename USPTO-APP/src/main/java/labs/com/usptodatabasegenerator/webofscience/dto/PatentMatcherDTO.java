package labs.com.usptodatabasegenerator.webofscience.dto;

import labs.com.usptodatabasegenerator.uspto.domain.entity.enums.ClassificationEnum;
import labs.com.usptodatabasegenerator.uspto.domain.entity.enums.ContentTypeEnum;
import labs.com.usptodatabasegenerator.uspto.domain.entity.enums.OperatorEnum;
import labs.com.usptodatabasegenerator.uspto.domain.entity.enums.PersonTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;
import java.util.List;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class PatentMatcherDTO {

    private List<String> persons;
    private PersonTypeEnum personsType;
    private OperatorEnum personOperator;

    private List<String> classifications;
    private ClassificationEnum classificationType;
    private OperatorEnum classificationOperator;

    private List<String> claims;
    private ContentTypeEnum contentType;
    private OperatorEnum contentOperator;
    
    private Date initialPublicationDate;
    private Date finalPublicationDate;

}
