package labs.com.usptodatabasegenerator.uspto.domain.service.impl;

import labs.com.usptodatabasegenerator.uspto.domain.entity.enums.ReferenceEnum;
import labs.com.usptodatabasegenerator.uspto.domain.entity.patent.PatentReference;
import labs.com.usptodatabasegenerator.uspto.domain.entity.patent.UsptoPatent;
import labs.com.usptodatabasegenerator.uspto.domain.service.PatentReferenceService;
import labs.com.usptodatabasegenerator.uspto.domain.xml.*;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PatentReferenceServiceImpl implements PatentReferenceService {
    public static boolean filterByType(PatentReference r, ReferenceEnum referenceType) {
     
        boolean result = false;
        
        if (referenceType.equals(ReferenceEnum.ALL)){
            result = true;
        } else if(referenceType.equals(ReferenceEnum.USPTO)){
            result = r.getCountry().equals("US");
        } else if (referenceType.equals(ReferenceEnum.FOREIGN)){
            result = !r.getCountry().equals("US");
        }
        
        return result;
    }

    @Override
    public List<PatentReference> buildPatentReferenceFromPatentFile(Patent patent) {

        List<Patcit> patentCitations = collectPatentCitations(patent);
        return buildPatentReferences(patentCitations);
    }

    @Override
    public void setRelashionship(UsptoPatent usptoPatent) {
        usptoPatent.getPatentReferences()
                .forEach(claim -> claim.setUsptoPatent(usptoPatent));
    }

    private List<PatentReference> buildPatentReferences(List<Patcit> patentCitationsFromXML) {
        List<PatentReference> patentCitationList = new ArrayList<>();
        
        if ( patentCitationsFromXML != null) {
            patentCitationList = patentCitationsFromXML
                    .stream()
                    .map(this::buildPatentReference)
                    .collect(Collectors.toList());
        }
        
        return patentCitationList;
    }

    private PatentReference buildPatentReference(Patcit patentCitation) {

        DocumentId documentId = patentCitation.getDocumentId();

        return PatentReference
                .builder()
                .docNumber(documentId.getDocumentNumber())
                .country(documentId.getCountry())
                .title(documentId.getName())
                .publicationDate(formatDate(documentId.getDate()))
                .kind(documentId.getKind())
                .build();
    }

    private LocalDate formatDate(String date) {

        Integer year = Integer.valueOf(date.substring(0, 4));

        Integer month = Integer.valueOf(date.substring(5, 6));
        month = (month.equals(0)) ? 1 : month;
        
        Integer dayOfMounth = Integer.valueOf(date.substring(7, 8));
        dayOfMounth = (dayOfMounth.equals(0)) ? 1 : month;

        return LocalDate.of(year,month,dayOfMounth);
    }

    private List<Patcit> collectPatentCitations(Patent patent) {

        List<Patcit> patentCitationList = new ArrayList<>();

        UsReferences usReferences = patent
                .getBibliographicDataGrant()
                .getUsReferences();

        if (usReferences != null){
            List<UsCitation> usCitationList = usReferences.getUsCitationList();
            
            if (usCitationList != null){
                patentCitationList = usCitationList
                        .stream()
                        .filter(usCitation -> usCitation.getPatcit() != null)
                        .map(UsCitation::getPatcit)
                        .collect(Collectors.toList());
            }
        }
        
        return patentCitationList;
    }
}
