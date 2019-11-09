package labs.com.usptodatabasegenerator.uspto.domain.entity.patent;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class UsptoPatentTest {

    @Test
    public void toWebOfScience() {

        // Given
        String expected = "US10165765";
        UsptoPatent usptoPatent = UsptoPatent.builder().documentNumber("10165765").build();

        // When
        String actual = usptoPatent.toWebOfScience();

        // Then
        assertThat(actual).isEqualTo(expected);
        
    }
}