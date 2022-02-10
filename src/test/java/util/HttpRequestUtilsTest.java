package util;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import util.HttpRequestUtils.Pair;

public class HttpRequestUtilsTest {

    @Test
    void getHttpMethod() {
        String httpRequestHeader = "GET /index.html HTTP/1.1\n" +
                "Host: localhost:8080\n" +
                "Connection: keep-alive\n";
        assertEquals("GET", HttpRequestUtils.getHttpMethod(httpRequestHeader));

        httpRequestHeader = "POST /index.html HTTP/1.1\n" +
                "Host: localhost:8080\n" +
                "Connection: keep-alive\n";
        assertEquals("POST", HttpRequestUtils.getHttpMethod(httpRequestHeader));
    }

    @Test
    public void getUrlPath() {
        String httpRequestHeader = "GET /index.html HTTP/1.1\n" +
                "Host: localhost:8080\n" +
                "Connection: keep-alive\n";
        assertEquals("/index.html", HttpRequestUtils.getUrlPath(httpRequestHeader));
    }

    @Test
    void getInfoMap() {
        String httpMethod = "GET";
        String url = "/user/create?userId=cih468&password=1234&name=%EC%B5%9C%EC%84%B1%ED%98%84&email=cih468%40naver.com";
        String httpRequestBody = "";
        Optional<Map<String, String>> infoMapOptional = HttpRequestUtils.getInfoMap(httpMethod, url, httpRequestBody);
        assertTrue(infoMapOptional.isPresent());

        Map<String, String> infoMap = infoMapOptional.get();
        assertEquals("cih468", infoMap.get("userId"));
        assertEquals("1234", infoMap.get("password"));
    }

    @Test
    void getMethodPath() {
        String url = "/index.html";
        assertEquals(HttpRequestUtils.getMethodPath(url), "/index.html");

        url = "/user/create?userId=cih468&password=1234&name=%EC%B5%9C%EC%84%B1%ED%98%84&email=cih468%40naver.com";
        assertEquals("/user/create", HttpRequestUtils.getMethodPath(url));
    }


    @Test
    public void parseQueryString() {
        String queryString = "userId=javajigi";
        Map<String, String> parameters = HttpRequestUtils.parseQueryString(queryString, "&", "=");
        assertThat(parameters.get("userId")).isEqualTo("javajigi");
        assertThat(parameters.get("password")).isNull();

        queryString = "userId=javajigi&password=password2";
        parameters = HttpRequestUtils.parseQueryString(queryString, "&", "=");
        assertThat(parameters.get("userId")).isEqualTo("javajigi");
        assertThat(parameters.get("password")).isEqualTo("password2");
    }

    @Test
    public void parseQueryString_null() {
        Map<String, String> parameters = HttpRequestUtils.parseQueryString(null, "&", "=");
        assertThat(parameters.isEmpty()).isTrue();

        parameters = HttpRequestUtils.parseQueryString("", "&", "=");
        assertThat(parameters.isEmpty()).isTrue();

        parameters = HttpRequestUtils.parseQueryString(" ", "&", "=");
        assertThat(parameters.isEmpty()).isTrue();
    }

    @Test
    public void parseQueryString_invalid() {
        String queryString = "userId=javajigi&password";
        Map<String, String> parameters = HttpRequestUtils.parseQueryString(queryString, "&", "=");
        assertThat(parameters.get("userId")).isEqualTo("javajigi");
        assertThat(parameters.get("password")).isNull();
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
    public void getKeyValue() {
        Pair pair = HttpRequestUtils.getKeyValue("userId=javajigi", "=");
        assertThat(pair).isEqualTo(new Pair("userId", "javajigi"));
    }

    @Test
    public void getKeyValue_invalid() {
        Pair pair = HttpRequestUtils.getKeyValue("userId", "=");
        assertThat(pair).isNull();
    }

    @Test
    public void parseHeader() {
        String header = "Content-Length: 59";
        Pair pair = HttpRequestUtils.parseHeader(header);
        assertThat(pair).isEqualTo(new Pair("Content-Length", "59"));
    }
}
