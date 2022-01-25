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
        assertThat(httpRequest.method() == HttpMethod.GET).isTrue();
        assertThat(httpRequest.url().equals("/index.html"));
        assertThat(httpRequest.getHttpVersion().equals("HTTP/1.1"));
    }

    @Test
    void httpRequestGetParameterTest(){
        String url = "GET /user/create?userId=javajigi&password=password&name=%EB%B0%95%EC%9E%AC%EC%84%B1&email=javajigi%40slipp.net HTTP/1.1\n";
        HttpRequest httpRequest = new HttpRequest(url);
        Map<String, String> params = httpRequest.params();
        assertThat(params.get("userId")).isEqualTo("javajigi");
        assertThat(params.get("password")).isEqualTo("password");
        assertThat(params.get("name")).isEqualTo("박재성");
        assertThat(params.get("email")).isEqualTo("javajigi@slipp.net");
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
        HttpRequest httpRequest = new HttpRequest(br);
        Map<String, String> body = httpRequest.body();
        assertThat(body.get("userId")).isEqualTo("javajigi");
        assertThat(body.get("password")).isEqualTo("password");
        assertThat(body.get("name")).isEqualTo("박재성");
        assertThat(body.get("email")).isEqualTo("javajigi@slipp.net");
    }

}
