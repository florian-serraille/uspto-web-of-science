package labs.com.usptodatabasegenerator.uspto.domain.entity.patent;

import labs.com.usptodatabasegenerator.uspto.domain.entity.enums.ClassificationEnum;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class InternationalClassificationTest {

    @Test
    public void toWebOfScience() {

        // Given
        String expected = "A01B 73/067 (20130101)";
        InternationalClassification internationalClassification = InternationalClassification.builder()
                .type(ClassificationEnum.CPC)
                .versionIndicator("20130101")
                .section("A")
                .className("01")
                .subClass("B")
                .mainGroup("73")
                .subGroup("067")
                .build();

        // When
        String actual = internationalClassification.toWebOfScience();

        // Then
        assertThat(actual).isEqualTo(expected);
    }
}