package http.response;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class HttpResponseBodyTest {
    @DisplayName("body가 비어있는 HttpResponseBody 객체 생성 테스트")
    @Test
    void emptyTest() {
        HttpResponseBody responseBody = HttpResponseBody.empty();

        Assertions.assertEquals(0, responseBody.length());
    }
}