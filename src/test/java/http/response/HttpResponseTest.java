package http.response;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("HttpResponse 테스트")
class HttpResponseTest {

    @DisplayName("200 OK HttpResponse")
    @Test
    void ok() throws IOException {
        //give
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(outputStream);
        String path = "/index.html";
        Map<String, Object> model = Map.of();
        Map<String, String> cookie = Map.of("cookie1", "firstCookie");
        //when
        HttpResponse result = HttpResponse.ok(path, model, cookie, dos);
        result.sendResponse();
        //then
        assertThat(outputStream.toString()).contains("200", "OK", "Content-Type", "Content-Length",
                "Set-Cookie");
    }

    @DisplayName("302 Found HttpResponse")
    @Test
    void found() throws IOException {
        //give
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(outputStream);
        String path = "/index.html";
        Map<String, String> cookie = Map.of("cookie1", "firstCookie");
        //when
        HttpResponse result = HttpResponse.found(path, cookie, dos);
        result.sendResponse();
        //then
        assertThat(outputStream.toString()).contains("302", "Found", "Location", "Set-Cookie");
    }

    @DisplayName("401 Unauthorized HttpResponse")
    @Test
    void unauthorized() throws IOException {
        //give
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(outputStream);
        String path = "/index.html";
        Map<String, Object> model = Map.of();
        //when
        HttpResponse result = HttpResponse.unauthorized(path, model, dos);
        result.sendResponse();
        //then
        assertThat(outputStream.toString()).contains("401", "Unauthorized", "Content-Type",
                "Content-Length");
    }

    @DisplayName("404 Not Found HttpResponse")
    @Test
    void notFound() throws IOException {
        //give
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(outputStream);
        //when
        HttpResponse result = HttpResponse.notFound(dos);
        result.sendResponse();
        //then
        assertThat(outputStream.toString()).contains("404", "Not Found", "Content-Type",
                "Content-Length");
    }

    @DisplayName("400 Bad Request HttpResponse")
    @Test
    void badRequest() throws IOException {
        //give
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(outputStream);
        //when
        HttpResponse result = HttpResponse.badRequest(dos);
        result.sendResponse();
        //then
        assertThat(outputStream.toString()).contains("400", "Bad Request", "Content-Type",
                "Content-Length");
    }
}
