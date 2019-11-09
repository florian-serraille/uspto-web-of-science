package labs.com.usptodatabasegenerator.uspto.domain.service;

import labs.com.usptodatabasegenerator.uspto.domain.entity.patent.Claim;
import labs.com.usptodatabasegenerator.uspto.domain.entity.patent.UsptoPatent;
import labs.com.usptodatabasegenerator.uspto.domain.xml.Patent;

import java.util.List;

public interface ClaimService {
    List<Claim> buildClaimListFromPatentFile(Patent patent);
    void setRelashionship(UsptoPatent usptoPatent);
}
