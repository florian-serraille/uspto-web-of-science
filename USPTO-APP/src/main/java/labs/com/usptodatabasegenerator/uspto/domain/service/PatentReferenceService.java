package labs.com.usptodatabasegenerator.uspto.domain.service;

import labs.com.usptodatabasegenerator.uspto.domain.entity.patent.PatentReference;
import labs.com.usptodatabasegenerator.uspto.domain.entity.patent.UsptoPatent;
import labs.com.usptodatabasegenerator.uspto.domain.xml.Patent;

import java.util.List;

public interface PatentReferenceService {
    List<PatentReference> buildPatentReferenceFromPatentFile(Patent patent);
    void setRelashionship(UsptoPatent usptoPatent);
}
