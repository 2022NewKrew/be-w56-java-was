package http;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RequestTest {

    @Test
    void parse_get() throws IOException {
        String raw = "GET /index.html HTTP/1.1\n" +
                "Host: localhost:8080\n" +
                "Connection: keep-alive\n" +
                "Accept: */*\n" +
                "";
        byte[] buf = raw.getBytes(StandardCharsets.UTF_8);
        ByteArrayInputStream is = new ByteArrayInputStream(buf);

        Request result = Request.parse(is);

        assertEquals("GET", result.getMethod());
        assertEquals("/index.html", result.getPath());
        assertEquals("HTTP/1.1", result.getVersion());
        assertEquals("localhost:8080", result.getHeader("Host"));
        assertEquals("keep-alive", result.getHeader("Connection"));
        assertEquals("*/*", result.getHeader("Accept"));
        assertEquals("", result.getBody());
    }

    @Test
    void parse_post() throws IOException {
        String raw = "POST /tasks HTTP/1.1\n" +
                "Host: example.org\n" +
                "Content-Type: application/x-www-form-urlencoded\n" +
                "Content-Length: 50\n" +
                "\n" +
                "title=Send%20report%20to%20manager&completed=false\n" +
                "";
        byte[] buf = raw.getBytes(StandardCharsets.UTF_8);
        ByteArrayInputStream is = new ByteArrayInputStream(buf);

        Request result = Request.parse(is);

        assertEquals("POST", result.getMethod());
        assertEquals("/tasks", result.getPath());
        assertEquals("HTTP/1.1", result.getVersion());
        assertEquals("example.org", result.getHeader("Host"));
        assertEquals("application/x-www-form-urlencoded", result.getHeader("Content-Type"));
        assertEquals("title=Send%20report%20to%20manager&completed=false", result.getBody());
    }
}
