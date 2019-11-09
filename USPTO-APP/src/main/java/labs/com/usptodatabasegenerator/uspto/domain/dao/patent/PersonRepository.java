package labs.com.usptodatabasegenerator.uspto.domain.dao.patent;

import labs.com.usptodatabasegenerator.uspto.domain.entity.enums.PersonTypeEnum;
import labs.com.usptodatabasegenerator.uspto.domain.entity.patent.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {

    @Query(value = "select p from Person p where " +
            "p.type = ?1 and " +
            "p.firstName = ?2 and " +
            "p.lastName = ?3 and " +
            "p.organizationName = ?4 and " +
            "p.country = ?5")
    Optional<Person> findByPerson(PersonTypeEnum type,
                                 String firstName,
                                 String lastName,
                                 String organizationName,
                                 String country);

    Optional<Person> findByTypeAndFirstNameAndLastNameAndOrganizationNameAndCountry(PersonTypeEnum type,
                                                                         String firstName,
                                                                         String lastName,
                                                                         String organizationName,
                                                                         String country);

}
