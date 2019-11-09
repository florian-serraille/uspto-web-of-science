package labs.com.usptodatabasegenerator.uspto.domain.service.impl;

import labs.com.usptodatabasegenerator.uspto.domain.entity.enums.ReferenceEnum;
import labs.com.usptodatabasegenerator.uspto.domain.entity.patent.OtherReference;
import labs.com.usptodatabasegenerator.uspto.domain.entity.patent.UsptoPatent;
import labs.com.usptodatabasegenerator.uspto.domain.service.OtherReferenceService;
import labs.com.usptodatabasegenerator.uspto.domain.xml.Nplcit;
import labs.com.usptodatabasegenerator.uspto.domain.xml.Patent;
import labs.com.usptodatabasegenerator.uspto.domain.xml.UsCitation;
import labs.com.usptodatabasegenerator.uspto.domain.xml.UsReferences;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OtherReferenceServiceImpl implements OtherReferenceService {
    public static boolean filterByType(ReferenceEnum referenceType) {
        
        return  (referenceType.equals(ReferenceEnum.OTHERS) || referenceType.equals(ReferenceEnum.ALL));
    }

    @Override
    public List<OtherReference> buildOtherReferenceFromPatentFile(Patent patent) {
        List<Nplcit> otherCitations = collectOtherCitations(patent);
        return buildOtherReferences(otherCitations);
    }

    @Override
    public void setRelashionship(UsptoPatent usptoPatent) {
        usptoPatent.getOtherReferences()
                .forEach(claim -> claim.setUsptoPatent(usptoPatent));
    }

    private List<OtherReference> buildOtherReferences(List<Nplcit> otherCitationsFromXML) {

        List<OtherReference> patentCitationList = new ArrayList<>();

        if ( otherCitationsFromXML != null) {
            patentCitationList = otherCitationsFromXML
                    .stream()
                    .map(this::buildOtherReference)
                    .map(this::truncDescription)
                    .collect(Collectors.toList());
        }
        
        return patentCitationList;
    }

    private OtherReference truncDescription(OtherReference otherReference) {
        String description = otherReference.getDescription();
        if (description != null && description.length() > 255){
            otherReference.setDescription(description.substring(0, 255));
        }
        return otherReference;
    }

    private OtherReference buildOtherReference(Nplcit nplcit) {
        return OtherReference
                .builder()
                .description(nplcit.getOthercit())
                .build();
    }

    private List<Nplcit> collectOtherCitations(Patent patent) {
        
        List<Nplcit> patentCitationList = new ArrayList<>();

        UsReferences usReferences = patent
                .getBibliographicDataGrant()
                .getUsReferences();

        if (usReferences != null){
            List<UsCitation> usCitationList = usReferences.getUsCitationList();

            if (usCitationList != null){
                patentCitationList = usCitationList
                        .stream()
                        .filter(usCitation -> usCitation.getNplcit() != null)
                        .map(UsCitation::getNplcit)
                        .collect(Collectors.toList());
            }
        }

        return patentCitationList;
    }
}
