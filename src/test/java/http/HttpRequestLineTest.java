package http;

import org.junit.jupiter.api.Test;

import java.net.URI;

import static org.junit.jupiter.api.Assertions.*;

class HttpRequestLineTest {

    @Test
    void testHttpRequestLineFromString() {
        HttpRequestLine requestLine = HttpRequestLine.from("GET /user/create HTTP/1.1");
        HttpRequestLine actual = new HttpRequestLine(HttpMethod.GET, URI.create("/user/create"), HttpVersion.HTTP_1_1);
        assertEquals(requestLine, actual);
    }
}
