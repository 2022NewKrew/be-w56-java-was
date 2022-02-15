package webserver.http.request.header;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RequestHeaderTest {

    RequestHeader requestHeaderByGetMethod = new RequestHeader("GET " +
            "/user/create?userId=cih468&password=1234&name=%EC%B5%9C%EC%84%B1%ED%98%84&email=cih468%40naver.com" +
            " HTTP/1.1\n" +
            "Host: localhost:8080\n" +
            "Connection: keep-alive\n" +
            "Accept: */*");

    RequestHeader requestHeaderByPostMethod = new RequestHeader("POST " +
            "/user/create HTTP/1.1\n" +
            "Host: localhost:8080\n" +
            "Connection: keep-alive\n" +
            "Content-Length: 85\n" +
            "Content-Type: application/x-www-form-urlencoded\n" +
            "Accept: */*");

    @Test
    void getUrlPath() {
        assertEquals("/user/create", requestHeaderByGetMethod.getUrlPath());
        assertEquals("/user/create", requestHeaderByPostMethod.getUrlPath());

    }

    @Test
    void getContentLength() {
        assertEquals(0, requestHeaderByGetMethod.getContentLength());
        assertEquals(85, requestHeaderByPostMethod.getContentLength());
    }

    @Test
    void getHttpMethod() {
        assertEquals("GET", requestHeaderByGetMethod.getHttpMethod());
        assertEquals("POST", requestHeaderByPostMethod.getHttpMethod());
    }
}