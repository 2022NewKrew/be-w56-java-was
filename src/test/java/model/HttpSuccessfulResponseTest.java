package model;

import java.io.IOException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class HttpSuccessfulResponseTest {

    @Test
    @DisplayName("[성공] HttpSuccessfulResponse 객체를 생성한다")
    void of() throws IOException {
        HttpStatus httpStatus = HttpStatus.OK;
        String url = "TEST URL";
        byte[] body = "TEST BODY".getBytes();

        HttpSuccessfulResponse.of(httpStatus, url, body);
    }
}
