package labs.com.usptodatabasegenerator.uspto.domain.entity.patent;

import labs.com.usptodatabasegenerator.uspto.domain.entity.enums.PersonTypeEnum;
import lombok.*;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "PERSON")
public class Person {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "TYPE")
    @Enumerated(EnumType.STRING)
    private PersonTypeEnum type;
    @Column(name = "FIRST_NAME")
    private String firstName;
    @Column(name = "LAST_NAME")
    private String lastName;
    @Column(name = "ORGANIZATION_NAME")
    private String organizationName;
    @Column(name = "COUNTRY")
    private String country;

    public static Person toLowerCase(Person person) {

        if (person != null) {
            person.setFirstName(StringUtils.lowerCase(person.getFirstName()));
            person.setLastName(StringUtils.lowerCase(person.getLastName()));
            person.setOrganizationName((StringUtils.lowerCase(person.getOrganizationName())));
            person.setCountry(StringUtils.lowerCase(person.getCountry()));
        }
        return person;
    }

    public String toWebOfScience() {

        StringBuilder builder = new StringBuilder();
        
        if (lastName != null && firstName != null){
            builder.append(lastName).append(",").append(firstName);
        }
        if (organizationName != null){
            if (builder.toString().length() > 0){
                builder.append(", ");
            }
            builder.append(organizationName);
        }
        
        builder.append(" (").append(country).append(")");
        
        return builder.toString().toUpperCase();
    }
}
