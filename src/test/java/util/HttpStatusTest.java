package util;

import org.junit.jupiter.api.Test;
import webserver.http.HttpStatus;

import static org.assertj.core.api.Assertions.assertThat;

class HttpStatusTest {

    @Test
    void valueOf() {
        HttpStatus httpStatus = HttpStatus.valueOf("OK");
        assertThat(httpStatus.toString()).isEqualTo("200 OK");
    }
}
