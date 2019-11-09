package labs.com.usptodatabasegenerator.uspto.domain.service.impl;

import labs.com.usptodatabasegenerator.uspto.domain.dao.patent.CustomUsptoPatentRepository;
import labs.com.usptodatabasegenerator.uspto.domain.dao.patent.UsptoPatentRepository;
import labs.com.usptodatabasegenerator.uspto.domain.entity.enums.PersonTypeEnum;
import labs.com.usptodatabasegenerator.uspto.domain.entity.patent.UsptoPatent;
import labs.com.usptodatabasegenerator.uspto.domain.service.*;
import labs.com.usptodatabasegenerator.uspto.domain.xml.Patent;
import labs.com.usptodatabasegenerator.webofscience.dto.PatentMatcherDTO;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class UsptoPatentServiceImpl implements UsptoPatentService {

    private final UsptoPatentRepository usptoPatentRepository;
    private final CustomUsptoPatentRepository customUsptoPatentRepository;
    private final PersonService personService;
    private final ClassificationService classificationService;
    private final ClaimService claimService;
    private final OtherReferenceService otherReferenceService;
    private final PatentReferenceService patentReferenceService;
    
    private static Long currentLoadItem;
    private static List<Long> usptoPatendIdList;

    public UsptoPatentServiceImpl(UsptoPatentRepository usptoPatentDAO,
                                  CustomUsptoPatentRepository customUsptoPatentRepository, PersonService personService,
                                  ClassificationServiceImpl classService,
                                  ClaimService claimService,
                                  OtherReferenceService otherReferenceService,
                                  PatentReferenceService patentReferenceService) {
        this.usptoPatentRepository = usptoPatentDAO;
        this.customUsptoPatentRepository = customUsptoPatentRepository;
        this.personService = personService;
        this.classificationService = classService;
        this.claimService = claimService;
        this.otherReferenceService = otherReferenceService;
        this.patentReferenceService = patentReferenceService;
        
        currentLoadItem = 0L;
        usptoPatendIdList = new ArrayList<>();
    }

    
    public static Long currentLoadItem() {
        return currentLoadItem;
    }

    public static void emptyItemCount() {
        currentLoadItem = 0L;
    }
    
    public static long getUsptoPatendIdList(){
        return usptoPatendIdList.size();
    }

    @Override
    @Transactional
    public void saveAll(List<UsptoPatent> usptoPatentList) {

        emptyItemCount();

        usptoPatentList.stream()
                .map(personService::mergeExistingPeople)
                .map(classificationService::mergeExistingCpc)
                .forEach(this::save);
    }

    @Override
    public UsptoPatent buildFromPatentFile(Patent patent) {

        UsptoPatent usptoPatent = buildUsptoPatent(patent);
        setRelashionship(usptoPatent);
        return usptoPatent;
    }

    @Override
    @Transactional
    public void deletePatentFile(UsptoPatent usptoPatent, String cleanDirectory) {
        FileUtils.deleteQuietly(new File(cleanDirectory, usptoPatent.getFileName()));
    }

    @Override
    public Integer findByPatentMatcher(PatentMatcherDTO patentMatcher) {
        usptoPatendIdList= customUsptoPatentRepository.findByPatentMatcher(patentMatcher);
        return usptoPatendIdList.size();
    }

    @Override
    public List<Long> getIdList() {
        return usptoPatendIdList;
    }

    @Override
    public boolean existResultFromQuery() {
        return usptoPatendIdList != null && !usptoPatendIdList.isEmpty();
    }

    private void save(UsptoPatent usptoPatent) {
        currentLoadItem++;
        usptoPatentRepository.save(usptoPatent);
    }

    private void setRelashionship(UsptoPatent usptoPatent) {
        claimService.setRelashionship(usptoPatent);
        patentReferenceService.setRelashionship(usptoPatent);
        otherReferenceService.setRelashionship(usptoPatent);

    }

    private UsptoPatent buildUsptoPatent(Patent patent) {
        return UsptoPatent
                .builder()
                .documentNumber(getIdFromDocumentNumber(patent))
                .title(getPatentNameFromIventionTitle(patent))
                .patentAbstract(getPatentAbstract(patent))
                .fileName(getUsptoFileName(patent))
                .publicationDate(getPublicationDate(patent))
                .inventors(personService.buildPersonSetFromPatentFile(patent, PersonTypeEnum.INVENTOR))
                .applicants(personService.buildPersonSetFromPatentFile(patent, PersonTypeEnum.APPLICANT))
                .assignees(personService.buildPersonSetFromPatentFile(patent, PersonTypeEnum.ASSIGNEE))
                .internationalClassifications(classificationService.buildCpcSetFromPatentFile(patent))
                .claims(claimService.buildClaimListFromPatentFile(patent))
                .patentReferences(patentReferenceService.buildPatentReferenceFromPatentFile(patent))
                .otherReferences(otherReferenceService.buildOtherReferenceFromPatentFile(patent))
                .build();
    }

    private String getPatentAbstract(Patent patent) {
        String patentAbstract = null; 
        if (patent.getPatentAbstract() != null && patent.getPatentAbstract().getAbstracts() != null){
            patentAbstract = patent.getPatentAbstract().getAbstracts().get(0);
        } else {
            patentAbstract = "NO ABSTRACT";
        }
        return patentAbstract;
    }

    private String getUsptoFileName(Patent patent) {
        return patent
                .getFile();
    }

    private LocalDate getPublicationDate(Patent patent) {
        return LocalDate.parse(patent.getPublicationDate(), DateTimeFormatter.BASIC_ISO_DATE);
    }

    private String getPatentNameFromIventionTitle(Patent patent) {
        String inventionTitle = patent.getBibliographicDataGrant()
                .getInventionTitle();

        return StringUtils.truncate(inventionTitle, 250);
    }

    private String getIdFromDocumentNumber(Patent patent) {
        return patent
                .getBibliographicDataGrant()
                .getPublicationReference()
                .getDocumentId()
                .getDocumentNumber();
    }
}
