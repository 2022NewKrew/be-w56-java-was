package util;

import static org.assertj.core.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;
import util.HttpRequestUtils.Pair;
import http.HttpMethod;

public class HttpRequestUtilsTest {

    @Test
    public void parseRequestLine() {
        List<String> requestLines = new ArrayList<>();
        requestLines.add("GET /index.html HTTP/1.1");
        requestLines.add("Host: localhost:8080");
        requestLines.add("Connection: keep-alive");

        String[] tokens = HttpRequestUtils.parseRequestLine(requestLines);
        assertThat(tokens[0]).isEqualTo("GET");
        assertThat(tokens[1]).isEqualTo("/index.html");
    }

    @Test
    void parseHttpMethod() {
        String methodToken = "GET";

        HttpMethod method = HttpRequestUtils.parseHttpMethod(methodToken);

        assertThat(method).isEqualTo(HttpMethod.GET);
    }

    @Test
    void parseHttpMethod_invalid() {
        String methodToken = "GETT";

        assertThatThrownBy(() -> HttpRequestUtils.parseHttpMethod(methodToken))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("No enum constant http.HttpMethod." + methodToken);
    }

    @Test
    void parsePath() {
        String targetToken = "/users/create?userId=javajigi";

        String path = HttpRequestUtils.parsePath(targetToken);

        assertThat(path).isEqualTo("/users/create");
    }

    @Test
    public void parseQueries() {
        String targetToken = "/users/create?userId=javajigi";
        Map<String, String> parameters = HttpRequestUtils.parseQueries(targetToken);
        assertThat(parameters.get("userId")).isEqualTo("javajigi");
        assertThat(parameters.get("password")).isNull();

        targetToken = "/create/user?userId=javajigi&password=password2";
        parameters = HttpRequestUtils.parseQueries(targetToken);
        assertThat(parameters.get("userId")).isEqualTo("javajigi");
        assertThat(parameters.get("password")).isEqualTo("password2");
    }

    @Test
    public void parseQueries_null() {
        Map<String, String> parameters = HttpRequestUtils.parseQueries("/index.html");
        assertThat(parameters.isEmpty()).isTrue();

        parameters = HttpRequestUtils.parseQueries("");
        assertThat(parameters.isEmpty()).isTrue();

        parameters = HttpRequestUtils.parseQueries(" ");
        assertThat(parameters.isEmpty()).isTrue();
    }

    @Test
    public void parseQueries_invalid() {
        String queryString = "/users/create?userId=javajigi&password";
        Map<String, String> parameters = HttpRequestUtils.parseQueries(queryString);
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
