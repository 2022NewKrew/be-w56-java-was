package util;

import model.MyHttpRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

class HttpRequestTest {

    @DisplayName("Request 첫 줄을 잘 저장하는 지 테스트")
    @Test
    void setRequestTest() {
        // given
        MyHttpRequest myHttpRequest = new MyHttpRequest();
        String request = "GET /index.html HTTP/1.1";

        // when
        myHttpRequest.setRequest(request);

        // then
        assertEquals(myHttpRequest.getMethod(), "GET");
        assertEquals(myHttpRequest.getUri(), "/index.html");
        assertEquals(myHttpRequest.getProtocol(), "HTTP/1.1");
    }

    @DisplayName("header를 map에 잘 저장하는 지 테스트")
    @ParameterizedTest
    @CsvSource({"Host: localhost:8080, Host, localhost:8080",
            "Connection: keep-alive, Connection, keep-alive",
            "Accept: */*, Accept, */*"})
    void setHeaderTest(String header, String key, String value) {
        // given
        MyHttpRequest myHttpRequest = new MyHttpRequest();

        // when
        myHttpRequest.setHeader(header);

        // then
        assertEquals(myHttpRequest.getHeader(key), value);
    }
}
