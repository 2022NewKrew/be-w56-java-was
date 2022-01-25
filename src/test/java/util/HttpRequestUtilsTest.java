package util;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

import http.Request;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
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

    @Test
    void parseRequest() throws IOException {
        // Given
        BufferedReader bufferedReader = Mockito.mock(BufferedReader.class);
        given(bufferedReader.readLine())
            .willReturn("GET /index.html HTTP/1.1")
            .willReturn("Host: localhost:8080")
            .willReturn("Connection: keep-alive")
            .willReturn("Accept: */*")
            .willReturn("");

        // When
        Request request = HttpRequestUtils.parseRequest(bufferedReader);

        // Then
        assertThat(request.getMethod())
            .isEqualTo("GET");
        assertThat(request.getTarget())
            .isEqualTo("/index.html");
        assertThat(request.getHeaderSize())
            .isEqualTo(3);
        assertThat(request.getHeader("Host"))
            .isEqualTo("localhost:8080");
        assertThat(request.getHeader("Connection"))
            .isEqualTo("keep-alive");
        assertThat(request.getHeader("Accept"))
            .isEqualTo("*/*");
    }
}
