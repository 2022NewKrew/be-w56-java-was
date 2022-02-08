package webserver.http;

import org.junit.jupiter.api.Test;
import util.HttpRequestUtils;

import java.io.*;
import java.net.http.HttpClient;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class HttpRequestTest {

    @Test
    void testHttpRequestWithEmptyBody() throws IOException {
        // given
        String requestString = "GET /index.html HTTP/1.1\n" +
                "Host: localhost:8080\n" +
                "Connection: keep-alive\n" +
                "Accept: text/css,*/*\n" +
                "\n";

        // when
        InputStream is = new ByteArrayInputStream(requestString.getBytes());
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        HttpRequest httpRequest = HttpRequestUtils.parseRequest(br);

        // then
        assertThat(httpRequest.method()).isEqualTo(HttpMethod.GET);
        assertThat(httpRequest.uri().toString()).isEqualTo("/index.html");
        assertThat(httpRequest.version()).hasValue(HttpClient.Version.HTTP_1_1);
        assertThat(httpRequest.headers())
                .extracting(
                        h -> h.getValues("Host"),
                        h -> h.getValues("Connection"),
                        h -> h.getValues("Accept"))
                .doesNotContainNull()
                .containsExactly(
                        List.of("localhost:8080"),
                        List.of("keep-alive"),
                        List.of("text/css", "*/*"));
        assertThat(httpRequest.body()).isEmpty();
    }

    @Test
    void testHttpRequestWithBody() throws IOException {
        // given
        String body = "userId=pug&password=123&name=pug.gg&email=pug.gg@kakaocorp.com";


        String requestString = "POST /user/create HTTP/1.1\n" +
                "Host: localhost:8080\n" +
                "Connection: keep-alive\n" +
                "Content-Length: " + body.length() + "\n" +
                "Content-Type: application/x-www-form-urlencoded\n" +
                "Accept: text/css,*/*\n" +
                "\n" +
                body + "\n";

        // when
        InputStream is = new ByteArrayInputStream(requestString.getBytes());
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        HttpRequest httpRequest = HttpRequestUtils.parseRequest(br);

        // then
        assertThat(httpRequest.method()).isEqualTo(HttpMethod.POST);
        assertThat(httpRequest.uri().toString()).isEqualTo("/user/create");
        assertThat(httpRequest.version()).hasValue(HttpClient.Version.HTTP_1_1);
        assertThat(httpRequest.headers())
                .extracting(
                        h -> h.getValues("Host"),
                        h -> h.getValues("Connection"),
                        h -> h.getValues("Content-Length"),
                        h -> h.getValues("Content-Type"),
                        h -> h.getValues("Accept"))
                .doesNotContainNull()
                .containsExactly(
                        List.of("localhost:8080"),
                        List.of("keep-alive"),
                        List.of(String.valueOf(body.length())),
                        List.of("application/x-www-form-urlencoded"),
                        List.of("text/css", "*/*"));
        assertThat(httpRequest.body()).isEqualTo(body);
    }
}
