package labs.com.usptodatabasegenerator.uspto.domain.service;

import labs.com.usptodatabasegenerator.uspto.domain.entity.patent.OtherReference;
import labs.com.usptodatabasegenerator.uspto.domain.entity.patent.UsptoPatent;
import labs.com.usptodatabasegenerator.uspto.domain.xml.Patent;

import java.util.List;

public interface OtherReferenceService {
    List<OtherReference> buildOtherReferenceFromPatentFile(Patent patent);
    void setRelashionship(UsptoPatent usptoPatent);
}
