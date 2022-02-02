package http.response;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Map;
import org.junit.jupiter.api.Test;

class HttpResponseTest {

    @Test
    void ok() throws IOException {
        //give
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(outputStream);
        String path = "/index.html";
        Map<String, String> model = Map.of();
        Map<String, String> cookie = Map.of("cookie1", "firstCookie");
        //when
        HttpResponse result = HttpResponse.ok(path, model, cookie, dos);
        result.sendResponse();
        //then
        assertThat(outputStream.toString()).contains("200", "OK", "Content-Type", "Content-Length",
                "Set-Cookie");
    }

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

    @Test
    void unauthorized() throws IOException {
        //give
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(outputStream);
        String path = "/index.html";
        Map<String, String> model = Map.of();
        //when
        HttpResponse result = HttpResponse.unauthorized(path, model, dos);
        result.sendResponse();
        //then
        assertThat(outputStream.toString()).contains("401", "Unauthorized", "Content-Type",
                "Content-Length");
    }

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
