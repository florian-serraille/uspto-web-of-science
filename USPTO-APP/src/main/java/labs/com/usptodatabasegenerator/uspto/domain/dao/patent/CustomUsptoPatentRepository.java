package labs.com.usptodatabasegenerator.uspto.domain.dao.patent;

import labs.com.usptodatabasegenerator.uspto.domain.entity.enums.ClassificationEnum;
import labs.com.usptodatabasegenerator.uspto.domain.entity.enums.ContentTypeEnum;
import labs.com.usptodatabasegenerator.uspto.domain.entity.enums.OperatorEnum;
import labs.com.usptodatabasegenerator.uspto.domain.entity.enums.PersonTypeEnum;
import labs.com.usptodatabasegenerator.webofscience.dto.PatentMatcherDTO;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Transactional
@Repository
public class CustomUsptoPatentRepository {

    private final EntityManager entityManager;

    public CustomUsptoPatentRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public List<Long> findByPatentMatcher(PatentMatcherDTO patentMatcher) {

        String query = "SELECT DISTINCT UP.DOCUMENT_NUMBER FROM PATENT UP \n" +
                "    JOIN PATENT_PEOPLE PP ON UP.DOCUMENT_NUMBER = PP.PATENT_DOCUMENT_NUMBER\n" +
                "    JOIN PERSON P ON PP.PERSON_ID = P.ID\n" +
                "    JOIN CLAIM C ON UP.DOCUMENT_NUMBER = C.PATENT_DOCUMENT_NUMBER\n" +
                "    JOIN PATENT_INTERNATIONAL_CLASSIFICATION PC ON UP.DOCUMENT_NUMBER = PC.PATENT_DOCUMENT_NUMBER\n" +
                "    JOIN INTERNATIONAL_CLASSIFICATION CL ON PC.INTERNATIONAL_CLASSIFICATION_ID = CL.ID\n" +
                "WHERE 1=1 ";
        
        query = getPersonClause(patentMatcher.getPersons(),
                patentMatcher.getPersonsType(),
                patentMatcher.getPersonOperator(),
                query);

        query = getClassificationClause(patentMatcher.getClassifications(),
                patentMatcher.getClassificationType(),
                patentMatcher.getClassificationOperator(),
                query);

        
        if (!ContentTypeEnum.ABSTRACT.equals(patentMatcher.getContentType())) {
            query = getClaimClause(patentMatcher.getClaims(),
                    patentMatcher.getContentOperator(),
                    query);
        }

        query = getDateClause(patentMatcher.getInitialPublicationDate(),
                patentMatcher.getFinalPublicationDate(),
                query);

        query = query.concat(";");

        Query nativeQuery = entityManager.createNativeQuery(query);

        return (List<Long>) nativeQuery.getResultStream().collect(Collectors.toList());
    }

    private String getClaimClause(List<String> claims, OperatorEnum contentOperator, String query) {
        String clause = "";

        claims = removeEmptyElement(claims);

        if (claims.size() > 0) {
            clause = "\nAND (" + getClaimsClause(claims, contentOperator) + ")";
        }
        return query.concat(clause);
    }

    private List<String> removeEmptyElement(List<String> list) {
        if (list != null) {
            list.remove(null);
            list.remove("");
            return list;
        } else {
            return new ArrayList<>();
        }
    }

    private String getClaimsClause(List<String> claims, OperatorEnum contentOperator) {
        return claims
                .stream()
                .map(String::toLowerCase)
                .map(this::clauseWhereClause)
                .collect(Collectors.joining(" " + contentOperator + " "));
    }

    private String clauseWhereClause(String claim) {
        return "C.CLAIM LIKE '%" + claim + "%'";
    }

    private String getDateClause(Date initialPublicationDate, Date finalPublicationDate, String query) {

        String clause = "";
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");

        if (initialPublicationDate != null || finalPublicationDate != null) {
            clause = "\nAND UP.PUBLICATION_DATE BETWEEN " +
                    ((initialPublicationDate != null) ? "str_to_date('" + format.format(initialPublicationDate) + "','%d/%m/%Y')" : "str_to_date('01/01/1975','%d/%m/%Y')") +
                    " AND " + ((finalPublicationDate != null) ? "str_to_date('" + format.format(finalPublicationDate) + "','%d/%m/%Y')" : "sysdate()");
        }

        return query.concat(clause);
    }

    private String getClassificationClause(List<String> classifications,
                                           ClassificationEnum classificationType,
                                           OperatorEnum operator,
                                           String query) {

        String clause = "";

        classifications = removeEmptyElement(classifications);

        if (classifications.size() > 0) {
            clause = "\nAND (" + getClassificationsClause(classifications, operator) + ")";
            query = query.concat(clause);
        }

        if (!clause.equals("") && !classificationType.equals(ClassificationEnum.ALL)) {
            clause = getClassificationTypeClause(classificationType);
            query = query.concat(clause);
        }

        return query;
    }

    private String getClassificationsClause(List<String> classifications, OperatorEnum operator) {
        return classifications
                .stream()
                .map(this::classificationWhereClause)
                .collect(Collectors.joining(" " + operator + " "));
    }

    private String getClassificationTypeClause(ClassificationEnum classificationType) {

        String classificationTypeClause = "";

        if (classificationType != null) {
            classificationTypeClause = "\nand CL.TYPE = '" + classificationType + "'";
        }
        return classificationTypeClause;
    }

    private String getPersonClause(List<String> players, PersonTypeEnum personType, OperatorEnum operator, String query) {

        String clause = "";

        players = removeEmptyElement(players);

        if (players.size() > 0) {
            clause = "\nAND (" + getPlayersClause(players, operator) + ")";
            query = query.concat(clause);
        }

        if (!clause.equals("") && !personType.equals(PersonTypeEnum.ALL)) {
            clause = getPlayerTypeClause(personType);
            query = query.concat(clause);
        }

        return query;
    }

    private String getPlayersClause(List<String> players, OperatorEnum operator) {
        return players
                .stream()
                .map(String::toLowerCase)
                .map(this::playerWhereClause)
                .collect(Collectors.joining(" " + operator + " "));
    }

    private String classificationWhereClause(String classification) {
        return "CONCAT(CL.SECTION, CL.CLASS_NAME, CL.SUB_CLASS, ' ', CL.MAIN_GROUP, '/', CL.SUB_GROUP) LIKE '%" + classification + "%'";
    }

    private String playerWhereClause(String player) {
        return "(P.FIRST_NAME LIKE '%" + player + "%'" +
                " OR P.LAST_NAME LIKE '%" + player + "%'" +
                " OR P.ORGANIZATION_NAME LIKE '%" + player + "%') ";
    }

    private String getPlayerTypeClause(PersonTypeEnum playerType) {

        String playerTypeClause = "";

        if (playerType != null && !playerType.equals(PersonTypeEnum.ALL)) {
            playerTypeClause = "\nAND P.TYPE = '" + playerType + "'";
        }
        return playerTypeClause;
    }
}
