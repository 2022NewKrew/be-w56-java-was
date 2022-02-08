package http;

import http.header.Header;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class HeaderTest {

    @Test
    void createRequestHeader() {
        Header input = Header.create("Host: localhost:8080");
        Header compare = Header.create("Host", "localhost:8080");

        assertThat(input.getFieldName()).isEqualTo(compare.getFieldName());
        assertThat(input.getFieldValue()).isEqualTo(compare.getFieldValue());
    }
}
