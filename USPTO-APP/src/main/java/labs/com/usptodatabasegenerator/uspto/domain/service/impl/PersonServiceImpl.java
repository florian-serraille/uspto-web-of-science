package labs.com.usptodatabasegenerator.uspto.domain.service.impl;

import labs.com.usptodatabasegenerator.uspto.domain.dao.patent.PersonRepository;
import labs.com.usptodatabasegenerator.uspto.domain.entity.enums.PersonTypeEnum;
import labs.com.usptodatabasegenerator.uspto.domain.entity.patent.Person;
import labs.com.usptodatabasegenerator.uspto.domain.entity.patent.UsptoPatent;
import labs.com.usptodatabasegenerator.uspto.domain.service.PersonService;
import labs.com.usptodatabasegenerator.uspto.domain.xml.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class PersonServiceImpl implements PersonService {

    private final PersonRepository personRepository;

    public PersonServiceImpl(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    @Override
    public Set<Person> buildPersonSetFromPatentFile(Patent patent, PersonTypeEnum personType) {

        return getAdressbookSetFromPatent(patent, personType).stream()
                .map(addressbook -> buildPersonFromAdressbook(addressbook, personType))
                .map(Person::toLowerCase)
                .collect(Collectors.toSet());

    }

    @Override
    public UsptoPatent mergeExistingPeople(UsptoPatent usptoPatent) {

        usptoPatent.setInventors(
                usptoPatent.getInventors().stream().map(this::mergePerson).collect(Collectors.toSet()));
        usptoPatent.setApplicants(
                usptoPatent.getApplicants().stream().map(this::mergePerson).collect(Collectors.toSet()));
        usptoPatent.setAssignees(
                usptoPatent.getAssignees().stream().map(this::mergePerson).collect(Collectors.toSet()));

        return usptoPatent;
    }

    private Person mergePerson(Person person) {
           return  personRepository
                   .findByTypeAndFirstNameAndLastNameAndOrganizationNameAndCountry(person.getType(),
                           person.getFirstName(),
                           person.getLastName(),
                           person.getOrganizationName(),
                           person.getCountry())
                    .orElse(person);
    }

    private Person buildPersonFromAdressbook(Addressbook addressbook, PersonTypeEnum personType) {
        return Person.builder()
                .type(personType)
                .firstName(StringUtils.abbreviate( addressbook.getFirstName(), 50))
                .lastName(StringUtils.abbreviate(addressbook.getLastName(), 50))
                .organizationName(StringUtils.abbreviate(addressbook.getOrgName(), 80))
                .country(StringUtils.abbreviate(getAddressFromAddressbook(addressbook), 20))
                .build();
    }

    private Set<Addressbook> getAdressbookSetFromPatent(Patent patent, PersonTypeEnum personType) {

        Set<Addressbook> addressbookSet;

        switch (personType) {
            case INVENTOR:
                addressbookSet = getInventorsFromPatent(patent).getInventorList()
                        .stream()
                        .map(Inventor::getAddressbook)
                        .collect(Collectors.toSet());
                break;

            case APPLICANT:
                addressbookSet = getApplicantsFromPatent(patent).getApplicantList().stream()
                        .map(Applicant::getAddressbook)
                        .collect(Collectors.toSet());
                break;

            case ASSIGNEE:
                addressbookSet = getAssignesFromPatent(patent).getAssigneeList().stream()
                        .map(Assignee::getAddressbook)
                        .collect(Collectors.toSet());
                break;

            default:
                throw new RuntimeException();
        }

        return addressbookSet;
    }

    private Assignees getAssignesFromPatent(Patent patent) {

        Assignees assignees = Optional.ofNullable(patent.getBibliographicDataGrant()
                .getAssignees())
                .orElseGet(() -> Assignees.builder().assigneeList(Collections.emptyList()).build());

        return checkValidAssignees(assignees);
    }

    private Assignees checkValidAssignees(Assignees assignees) {
        assignees.setAssigneeList(assignees.getAssigneeList().stream()
                .filter(assignee -> assignee.getAddressbook() != null)
                .collect(Collectors.toList()));
        return assignees;
    }

    private Inventors getInventorsFromPatent(Patent patent) {
        return patent.getBibliographicDataGrant().getParties().getInventors();
    }

    private Applicants getApplicantsFromPatent(Patent patent) {
        return patent.getBibliographicDataGrant().getParties().getApplicants();
    }


    private String getAddressFromAddressbook(Addressbook addressbook) {
        return Optional.ofNullable(addressbook.getAddress())
                .orElseGet(() -> Address.builder().build())
                .getCountry();
    }

    public static Boolean filterByType(Person person, PersonTypeEnum personType){
        boolean result = true;
        if (!PersonTypeEnum.ALL.equals(personType)){
            result = personType.equals(person.getType());
        }
        return result;
    }
}
