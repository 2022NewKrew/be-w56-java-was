package webserver.http.request.header;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HeaderMapTest {

    HeaderMap headerMap = new HeaderMap("POST " +
            "/user/create HTTP/1.1\n" +
            "Host: localhost:8080\n" +
            "Connection: keep-alive\n" +
            "Content-Length: 85\n" +
            "Content-Type: application/x-www-form-urlencoded\n" +
            "Accept: */*");

    @Test
    void containsKey() {
        assertTrue(headerMap.containsKey("Host"));
    }

    @Test
    void get() {
        assertEquals("85", headerMap.get("Content-Length"));
    }
}