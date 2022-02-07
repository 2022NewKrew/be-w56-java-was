package util;

import model.MyHttpRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import webserver.enums.HttpMethod;

import java.io.IOException;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class HttpRequestTest {

    @DisplayName("Request 첫 줄을 잘 저장하는 지 테스트")
    @Test
    void setRequestTest() throws IOException {
        // given
        MyHttpRequest myHttpRequest = new MyHttpRequest();
        String request = "GET /index.html HTTP/1.1";

        // when
        myHttpRequest.setRequest(request);

        // then
        assertEquals(myHttpRequest.getMethod(), HttpMethod.GET);
        assertEquals(myHttpRequest.getUri(), "/index.html");
        assertEquals(myHttpRequest.getProtocol(), "HTTP/1.1");
    }

    @DisplayName("URL을 잘 분리하는 지 확인")
    @Test
    void setUriTest() {
        // given
        MyHttpRequest myHttpRequest = new MyHttpRequest();
        String request = "/user/create?userId=javajigi&password=password&name=%EB%B0%95%EC%9E%AC%EC%84%B1&email=javajigi%40slipp.net";

        // when
        myHttpRequest.setUri(request);

        // then
        String uri = myHttpRequest.getUri();
        Map<String, String> parameters = myHttpRequest.getParameters();
        assertEquals(uri, "/user/create");
        assertEquals(parameters.get("userId"), "javajigi");
        assertEquals(parameters.get("password"), "password");
        assertEquals(parameters.get("name"), "%EB%B0%95%EC%9E%AC%EC%84%B1");
        assertEquals(parameters.get("email"), "javajigi%40slipp.net");
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

    @DisplayName("request 포맷이 잘못된 경우")
    @Test
    void setRequestExceptionTest() {
        MyHttpRequest myHttpRequest = new MyHttpRequest();
        String request = "GET /index.html";

        try {
            myHttpRequest.setRequest(request);
            fail();
        } catch (IOException ignored) {}
    }
}
