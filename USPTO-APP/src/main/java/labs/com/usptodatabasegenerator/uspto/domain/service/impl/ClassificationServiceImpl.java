package labs.com.usptodatabasegenerator.uspto.domain.service.impl;

import labs.com.usptodatabasegenerator.uspto.domain.dao.patent.ClassificationRepository;
import labs.com.usptodatabasegenerator.uspto.domain.entity.enums.ClassificationEnum;
import labs.com.usptodatabasegenerator.uspto.domain.entity.patent.InternationalClassification;
import labs.com.usptodatabasegenerator.uspto.domain.entity.patent.UsptoPatent;
import labs.com.usptodatabasegenerator.uspto.domain.service.ClassificationService;
import labs.com.usptodatabasegenerator.uspto.domain.xml.CpcIpcr;
import labs.com.usptodatabasegenerator.uspto.domain.xml.Cpcs;
import labs.com.usptodatabasegenerator.uspto.domain.xml.Ipcrs;
import labs.com.usptodatabasegenerator.uspto.domain.xml.Patent;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class ClassificationServiceImpl implements ClassificationService {

    private final ClassificationRepository cpcRepository;

    public ClassificationServiceImpl(ClassificationRepository cpcRepository) {
        this.cpcRepository = cpcRepository;
    }

    public static boolean filterByType(InternationalClassification c, ClassificationEnum contentType) {
        
        Boolean result = true;
        if (!contentType.equals(ClassificationEnum.ALL)){
            result = c.getType().equals(contentType);
        }
        
        return result;
    }

    @Override
    public Set<InternationalClassification> buildCpcSetFromPatentFile(Patent patent) {

        Set<InternationalClassification> internationalClassifications = new HashSet<>();

        internationalClassifications.addAll(getCpcClassificationListromPatent(patent)
                .stream()
                .map(classificationCpcIcpr -> buildEntityFromClassificationClass(classificationCpcIcpr, ClassificationEnum.CPC))
                .collect(Collectors.toSet()));

        internationalClassifications.addAll(getIcprClassificationListromPatent(patent)
                .stream()
                .map((CpcIpcr classificationCpcIpcr) -> buildEntityFromClassificationClass(classificationCpcIpcr, ClassificationEnum.ICPR))
                .collect(Collectors.toSet()));

        return internationalClassifications;
    }

    @Override
    public UsptoPatent mergeExistingCpc(UsptoPatent usptoPatent) {
        usptoPatent.setInternationalClassifications(
                usptoPatent.getInternationalClassifications().stream()
                        .map(this::mergeCpc)
                        .collect(Collectors.toSet()));
        return usptoPatent;
    }

    private InternationalClassification mergeCpc(InternationalClassification internationalClassification){
        try {
            InternationalClassification c = cpcRepository.findOne(Example.of(internationalClassification))
                    .orElse(internationalClassification);
            return c;
        } catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    private InternationalClassification buildEntityFromClassificationClass(CpcIpcr classificationCpcIpcr, ClassificationEnum classificationType) {
        return InternationalClassification.builder()
                .type(classificationType)
                .versionIndicator((classificationType.equals(ClassificationEnum.CPC)) ?
                        classificationCpcIpcr.getCpcVersionIndicator().getDate() :
                        classificationCpcIpcr.getIpcVersionIndicator().getDate())
                .section(classificationCpcIpcr.getSection())
                .className(classificationCpcIpcr.getClassName())
                .subClass(classificationCpcIpcr.getSubClass())
                .mainGroup(classificationCpcIpcr.getMainGroup())
                .subGroup(classificationCpcIpcr.getSubGroup())
                .build();
    }

    private List<CpcIpcr> getCpcClassificationListromPatent(Patent patent){

        List<CpcIpcr> classificationList = new ArrayList<>();

        Cpcs classificationsCpc = patent.getBibliographicDataGrant().getClassificationsCpc();

        if (classificationsCpc == null) {
            return classificationList;
        }

        if (classificationsCpc.getMainCpc() != null &&
                classificationsCpc.getMainCpc().getClassificationCpcIpcr() != null) {
            classificationList.add(classificationsCpc.getMainCpc().getClassificationCpcIpcr());
        }
        if (classificationsCpc.getFurtherCpc() != null &&
                classificationsCpc.getFurtherCpc().getClassificationCpcIpcrList() != null) {
            classificationList.addAll(classificationsCpc.getFurtherCpc().getClassificationCpcIpcrList());
        }

        return classificationList;
    }

    private List<CpcIpcr> getIcprClassificationListromPatent(Patent patent){

        List<CpcIpcr> classificationList = new ArrayList<>();

        Ipcrs classificationsIcpr = patent.getBibliographicDataGrant().getClassificationsIpcr();

        if(classificationsIcpr != null && classificationsIcpr.getIpcrList() != null){
            classificationList.addAll(classificationsIcpr.getIpcrList());
        }

        return classificationList;
    }

}
