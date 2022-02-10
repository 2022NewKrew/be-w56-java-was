package model;

import java.io.IOException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class HttpClientErrorResponseTest {

    @Test
    @DisplayName("[성공] HttpClientErrorResponse 객체를 생성한다")
    void of() throws IOException {
        HttpStatus httpStatus = HttpStatus.CONFLICT;
        byte[] body = "TEST BODY".getBytes();

        HttpClientErrorResponse.of(httpStatus, body);
    }
}
