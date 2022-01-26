package util;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class HttpStatusTest {

    @Test
    void valueOf() {
        HttpStatus httpStatus = HttpStatus.valueOf("OK");
        assertThat(httpStatus.toString()).isEqualTo("200 OK");
    }
}
