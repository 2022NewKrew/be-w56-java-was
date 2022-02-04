package http.request;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HttpRequestLineTest {
    @DisplayName("HttpRequestLine Parsing 테스트 (query 존재)")
    @Test
    void parseRequestLineTest() {
        String line = "GET /index.html?key=value&key2=value2 HTTP/1.1";
        HttpRequestLine requestLine = HttpRequestLine.parseRequestLine(line);

        Assertions.assertEquals("GET", requestLine.method());
        Assertions.assertEquals("/index.html", requestLine.path());
        Assertions.assertEquals("value", requestLine.queryString().get("key"));
        Assertions.assertEquals("value2", requestLine.queryString().get("key2"));
    }

    @DisplayName("HttpRequestLine Parsing 테스트 (query 존재X)")
    @Test
    void parseRequestLineWithoutQueryTest() {
        String line = "GET /index.html HTTP/1.1";
        HttpRequestLine requestLine = HttpRequestLine.parseRequestLine(line);

        Assertions.assertEquals("GET", requestLine.method());
        Assertions.assertEquals("/index.html", requestLine.path());
        Assertions.assertEquals(null, requestLine.queryString());
    }
}