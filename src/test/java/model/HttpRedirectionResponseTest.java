package model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class HttpRedirectionResponseTest {

    @Test
    @DisplayName("[성공] HttpRedirectionResponse 객체를 생성한다")
    void of() {
        HttpStatus httpStatus = HttpStatus.FOUND;
        String url = "TEST URL";

        HttpRedirectionResponse.of(httpStatus, url);
    }
}
