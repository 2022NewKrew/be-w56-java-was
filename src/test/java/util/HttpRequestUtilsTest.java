package util;

import static org.assertj.core.api.Assertions.*;

import java.io.IOException;
import java.util.Map;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import util.HttpRequestUtils.Pair;

public class HttpRequestUtilsTest {
    @Test
    public void parseQueryString() {
        String queryString = "userId=javajigi";
        Map<String, String> parameters = HttpRequestUtils.parseQueryString(queryString);
        assertThat(parameters.get("userId")).isEqualTo("javajigi");
        assertThat(parameters.get("password")).isNull();

        queryString = "userId=javajigi&password=password2";
        parameters = HttpRequestUtils.parseQueryString(queryString);
        assertThat(parameters.get("userId")).isEqualTo("javajigi");
        assertThat(parameters.get("password")).isEqualTo("password2");
    }

    @Test
    public void parseQueryString_null() {
        Map<String, String> parameters = HttpRequestUtils.parseQueryString(null);
        assertThat(parameters.isEmpty()).isTrue();

        parameters = HttpRequestUtils.parseQueryString("");
        assertThat(parameters.isEmpty()).isTrue();

        parameters = HttpRequestUtils.parseQueryString(" ");
        assertThat(parameters.isEmpty()).isTrue();
    }

    @Test
    public void parseQueryString_invalid() {
        String queryString = "userId=javajigi&password";
        Map<String, String> parameters = HttpRequestUtils.parseQueryString(queryString);
        assertThat(parameters.get("userId")).isEqualTo("javajigi");
        assertThat(parameters.get("password")).isNull();
    }

    @Test
    public void parseQueryUrl() {
        String queryUrl = "/user/create?userId=javajigi&password=1234&name=자바지기&email=javajigi@slipp.net";
        Map<String, String> parameters = HttpRequestUtils.parseQueryUrl(queryUrl);
        assertThat(parameters.get("userId")).isEqualTo("javajigi");
        assertThat(parameters.get("password")).isEqualTo("1234");
        assertThat(parameters.get("name")).isEqualTo("자바지기");
        assertThat(parameters.get("email")).isEqualTo("javajigi@slipp.net");
    }

    @Test
    public void parseQueryUrl_null() {
        Map<String, String> parameters = HttpRequestUtils.parseQueryUrl(null);
        assertThat(parameters.isEmpty()).isTrue();

        parameters = HttpRequestUtils.parseQueryUrl("");
        assertThat(parameters.isEmpty()).isTrue();

        parameters = HttpRequestUtils.parseQueryUrl(" ");
        assertThat(parameters.isEmpty()).isTrue();
    }

    @Test
    public void parseQueryUrl_noQueryString() {
        String queryUrl = "/user/create";
        Map<String, String> parameters = HttpRequestUtils.parseQueryUrl(queryUrl);
        assertThat(parameters.isEmpty()).isTrue();
    }

    @Test
    public void parseRequest() throws IOException {
        String request = "GET /index.html HTTP/1.1";
        Map<String, String> parameters = HttpRequestUtils.parseRequest(request);
        assertThat(parameters.get("httpMethod")).isEqualTo("GET");
        assertThat(parameters.get("httpUrl")).isEqualTo("/index.html");
        assertThat(parameters.get("httpProtocol")).isEqualTo("HTTP/1.1");
    }

    @Test
    public void parseRequest_null() throws IOException {
        Map<String, String> parameters = HttpRequestUtils.parseRequest(null);
        assertThat(parameters.isEmpty()).isTrue();

        parameters = HttpRequestUtils.parseRequest("");
        assertThat(parameters.isEmpty()).isTrue();
    }

    @Test()
    public void parseRequest_lengthNotThree() {
        String request = "GET /index.html";
        Assertions.assertThrows(IOException.class, () -> HttpRequestUtils.parseRequest(request));
    }

    @Test
    public void parseCookies() {
        String cookies = "logined=true; JSessionId=1234";
        Map<String, String> parameters = HttpRequestUtils.parseCookies(cookies);
        assertThat(parameters.get("logined")).isEqualTo("true");
        assertThat(parameters.get("JSessionId")).isEqualTo("1234");
        assertThat(parameters.get("session")).isNull();
    }

    @Test
    public void getKeyValue() throws Exception {
        Pair pair = HttpRequestUtils.getKeyValue("userId=javajigi", "=");
        assertThat(pair).isEqualTo(new Pair("userId", "javajigi"));
    }

    @Test
    public void getKeyValue_invalid() throws Exception {
        Pair pair = HttpRequestUtils.getKeyValue("userId", "=");
        assertThat(pair).isNull();
    }

    @Test
    public void parseHeader() throws Exception {
        String header = "Content-Length: 59";
        Pair pair = HttpRequestUtils.parseHeader(header);
        assertThat(pair).isEqualTo(new Pair("Content-Length", "59"));
    }
}
