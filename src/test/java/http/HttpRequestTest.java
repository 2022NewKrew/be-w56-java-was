package http;

import org.junit.jupiter.api.Test;

import java.io.*;
import java.net.URI;

import static org.junit.jupiter.api.Assertions.*;

class HttpRequestTest {

    @Test
    void testSimpleRequest() throws IOException {
        String requestString = "GET /users HTTP/1.1\n" +
                "Host: localhost:8080\n" +
                "User-Agent: curl/7.64.1\n" +
                "Accept: */*";
        HttpRequest request = fromString(requestString);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Host: localhost:8080");
        headers.add("User-Agent: curl/7.64.1");
        headers.add("Accept: */*");

        assertEquals(request.getMethod(), HttpMethod.GET);
        assertEquals(request.getUri(), URI.create("/users"));
        assertEquals(request.getVersion(), HttpVersion.HTTP_1_1);
        assertEquals(request.getHeaders(), headers);
        assertNull(request.getBody());
    }

    private HttpRequest fromString(String requestString) throws IOException {
        InputStream in = new ByteArrayInputStream(requestString.getBytes());
        return HttpRequest.from(in);
    }

    @Test
    void testUrlWithQueryString() throws IOException {
        String requestString = "GET /user/create?userId=javajigi&password=password&name=%EB%B0%95%EC%9E%AC%EC%84%B1&email=javajigi%40slipp.net HTTP/1.1\n" +
                "Host: localhost:8080\n" +
                "Connection: keep-alive\n" +
                "Accept: */*";
        HttpRequest request = fromString(requestString);
        assertEquals(request.getUri(), URI.create("/user/create?userId=javajigi&password=password&name=%EB%B0%95%EC%9E%AC%EC%84%B1&email=javajigi%40slipp.net"));
    }

    @Test
    void testPostWithBody() throws IOException {
        String requestString = "POST /user/create HTTP/1.1\n" +
                "Host: localhost:8080\n" +
                "Connection: keep-alive\n" +
                "Content-Length: 93\n" +
                "Content-Type: application/x-www-form-urlencoded\n" +
                "Accept: */*\n" +
                "\n" +
                "userId=javajigi&password=password&name=%EB%B0%95%EC%9E%AC%EC%84%B1&email=javajigi%40slipp.net";
        HttpRequest request = fromString(requestString);
        assertEquals(request.getBody(), "userId=javajigi&password=password&name=%EB%B0%95%EC%9E%AC%EC%84%B1&email=javajigi%40slipp.net");
    }
}
