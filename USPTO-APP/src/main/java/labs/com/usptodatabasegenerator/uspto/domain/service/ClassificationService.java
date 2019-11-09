package labs.com.usptodatabasegenerator.uspto.domain.service;

import labs.com.usptodatabasegenerator.uspto.domain.entity.patent.InternationalClassification;
import labs.com.usptodatabasegenerator.uspto.domain.entity.patent.UsptoPatent;
import labs.com.usptodatabasegenerator.uspto.domain.xml.CpcIpcr;
import labs.com.usptodatabasegenerator.uspto.domain.xml.Patent;

import java.util.Set;

public interface ClassificationService {

    /**
     * Build a Set of {@link InternationalClassification} extracting all {@link CpcIpcr} from {@link Patent}
     */
    Set<InternationalClassification> buildCpcSetFromPatentFile(Patent patent);

    UsptoPatent mergeExistingCpc(UsptoPatent usptoPatent);
}
