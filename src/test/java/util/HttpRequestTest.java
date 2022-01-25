package util;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class HttpRequestTest {

    private static final Logger log = LoggerFactory.getLogger(HttpRequestTest.class);


    @Test
    void initHttpRequest(){
        String path = "GET /index.html HTTP/1.1";
        HttpRequest httpRequest = new HttpRequest(path);
        assertThat(httpRequest.method()).isSameAs(HttpMethod.GET);
        assertThat(httpRequest.url()).isEqualTo("/index.html");
        assertThat(httpRequest.getHttpVersion()).isEqualTo("HTTP/1.1");
    }

    @Test
    void httpRequestGetParameterTest() {
        String url = "GET /user/create?userId=javajigi&password=password&name=%EB%B0%95%EC%9E%AC%EC%84%B1&email=javajigi%40slipp.net HTTP/1.1\n";
        HttpRequest httpRequest = new HttpRequest(url);
        Map<String, String> params = httpRequest.params();

        assertThat(params).containsEntry("userId", "javajigi")
                .containsEntry("password", "password")
                .containsEntry("name", "박재성")
                .containsEntry("email", "javajigi@slipp.net");

    }

    @Test
    void httpPostRequestTest() throws IOException {
        String request = "POST /user/create HTTP/1.1\n" +
                "Host: localhost:8080\n" +
                "Connection: keep-alive\n" +
                "Content-Length: 93\n" +
                "Content-Type: application/x-www-form-urlencoded\n" +
                "Accept: */*\n" +
                "\n" +
                "userId=javajigi&password=password&name=박재성&email=javajigi@slipp.net\n";

        InputStream is = new ByteArrayInputStream(request.getBytes(StandardCharsets.UTF_8));
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        HttpRequest httpRequest = HttpRequestUtils.parseRequest(br);
        Map<String, String> body = httpRequest.body();

        assertThat(body).containsEntry("userId", "javajigi")
                .containsEntry("password", "password")
                .containsEntry("name", "박재성")
                .containsEntry("email", "javajigi@slipp.net");

    }

}
