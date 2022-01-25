package util;

import model.RequestHeader;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

class HttpRequestHeaderUtilsTest {

    @DisplayName("Request 첫 줄을 잘 저장하는 지 테스트")
    @Test
    void setRequestTest() {
        // given
        RequestHeader requestHeader = new RequestHeader();
        String request = "GET /index.html HTTP/1.1";

        // when
        HttpRequestHeaderUtils.setRequest(requestHeader, request);

        // then
        assertEquals(requestHeader.getMethod(), "GET");
        assertEquals(requestHeader.getUri(), "/index.html");
        assertEquals(requestHeader.getProtocol(), "HTTP/1.1");
    }

    @DisplayName("header를 map에 잘 저장하는 지 테스트")
    @ParameterizedTest
    @CsvSource({"Host: localhost:8080, Host, localhost:8080",
            "Connection: keep-alive, Connection, keep-alive",
            "Accept: */*, Accept, */*"})
    void setHeaderTest(String header, String key, String value) {
        // given
        RequestHeader requestHeader = new RequestHeader();

        // when
        HttpRequestHeaderUtils.setHeader(requestHeader, header);

        // then
        assertEquals(requestHeader.getHeader(key), value);
    }
}
