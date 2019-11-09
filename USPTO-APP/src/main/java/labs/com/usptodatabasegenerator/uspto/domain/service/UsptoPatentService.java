package labs.com.usptodatabasegenerator.uspto.domain.service;

import labs.com.usptodatabasegenerator.uspto.domain.entity.patent.UsptoPatent;
import labs.com.usptodatabasegenerator.uspto.domain.xml.Patent;
import labs.com.usptodatabasegenerator.webofscience.dto.PatentMatcherDTO;

import java.util.List;

public interface UsptoPatentService {

    void saveAll(List<UsptoPatent> usptoPatentList);
    UsptoPatent buildFromPatentFile(Patent patent);
    void deletePatentFile(UsptoPatent usptoPatent, String cleanDirectory);
    Integer findByPatentMatcher(PatentMatcherDTO patentMatcher);
    List<Long> getIdList();
    boolean existResultFromQuery();
}
