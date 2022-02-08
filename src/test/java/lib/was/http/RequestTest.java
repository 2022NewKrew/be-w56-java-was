package lib.was.http;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

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

        Map<String, String> headers = Map.of(
                "Host", "localhost:8080",
                "Connection", "keep-alive",
                "Accept", "*/*"
        );
        assertEquals(
                Request.newBuilder()
                        .method(Method.GET)
                        .locator(Locator.parse("/index.html"))
                        .version(Version.HTTP_1_1)
                        .headers(new Headers(headers))
                        .body(null)
                        .build(),
                result
        );
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

        Map<String, String> headers = Map.of(
                "Host", "example.org",
                "Content-Type", "application/x-www-form-urlencoded",
                "Content-Length", "50"
        );
        assertEquals(
                Request.newBuilder()
                        .method(Method.POST)
                        .locator(Locator.parse("/tasks"))
                        .version(Version.HTTP_1_1)
                        .headers(new Headers(headers))
                        .body("title=Send report to manager&completed=false")
                        .build(),
                result
        );
    }
}
