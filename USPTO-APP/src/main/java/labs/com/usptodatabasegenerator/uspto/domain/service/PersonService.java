package labs.com.usptodatabasegenerator.uspto.domain.service;


import labs.com.usptodatabasegenerator.uspto.domain.entity.enums.PersonTypeEnum;
import labs.com.usptodatabasegenerator.uspto.domain.entity.patent.Person;
import labs.com.usptodatabasegenerator.uspto.domain.entity.patent.UsptoPatent;
import labs.com.usptodatabasegenerator.uspto.domain.xml.Patent;

import java.util.Set;

public interface PersonService {

    /**
     * Build a Set of {@link Person} extracting all PersonTypeEnum.INVENTOR from {@link Patent}
     */
    Set<Person> buildPersonSetFromPatentFile(Patent patent, PersonTypeEnum personType);

    UsptoPatent mergeExistingPeople(UsptoPatent usptoPatent);
}
