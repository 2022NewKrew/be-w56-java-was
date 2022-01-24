package webserver;

import http.HttpRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;


class HttpRequestTest {

    @Test
    @DisplayName("초기화 테스트")
    void testOfInitialize() throws IOException {
        String httpRequestString = "GET /index.html HTTP/1.1\n" +
                "Host: localhost:8080\n" +
                "Connection: keep-alive\n" +
                "Accept: */*";
        InputStream inputStream = new ByteArrayInputStream(httpRequestString.getBytes(StandardCharsets.UTF_8));
        HttpRequest httpRequest = new HttpRequest(inputStream);
        assertThat(httpRequest.getUri()).isEqualTo("/index.html");
        assertThat(httpRequest.getHeader("Host").get()).isEqualTo("localhost:8080");
        assertThat(httpRequest.getHeader("Connection").get()).isEqualTo("keep-alive");
        assertThat(httpRequest.getHeader("Accept").get()).isEqualTo("*/*");
    }
}