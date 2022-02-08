package http;

import http.header.FieldValue;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class FieldValueTest {
    @Test
    void equals() {
        FieldValue test1 = new FieldValue("www.google.com");
        FieldValue test2 = new FieldValue("www.google.com");
        FieldValue test3 = new FieldValue("www.daum.com");

        assertThat(test1).isEqualTo(test2);
        assertThat(test1).isNotEqualTo(test3);
    }
}
