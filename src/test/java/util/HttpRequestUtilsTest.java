package util;

import static org.assertj.core.api.Assertions.*;

import exception.InvalidParameterKeyException;
import http.request.Queries;
import http.request.RequestLine;
import java.util.Map;
import org.junit.jupiter.api.Test;
import util.HttpRequestUtils.Pair;
import http.HttpMethod;

public class HttpRequestUtilsTest {

    @Test
    public void parseRequestLine() {
        String requestLineString = "GET /index.html HTTP/1.1";

        RequestLine requestLine = HttpRequestUtils.parseRequestLine(requestLineString);

        assertThat(requestLine.getMethod()).isEqualTo(HttpMethod.GET);
        assertThat(requestLine.getPath()).isEqualTo("/index.html");
        assertThat(requestLine.getQueries().getParams()).hasSize(0);
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
        String targetToken = "/create/user?userId=javajigi&password=password2";
        Queries queries = HttpRequestUtils.parseQueries(targetToken);
        assertThat(queries.getValue("userId")).isEqualTo("javajigi");
        assertThat(queries.getValue("password")).isEqualTo("password2");
    }

    @Test
    public void parseQueries_null() {
        Queries queries1 = HttpRequestUtils.parseQueries("/index.html");
        assertThat(queries1.getParams()).hasSize(0);

        Queries queries2 = HttpRequestUtils.parseQueries("/index.html");
        assertThat(queries2.getParams()).hasSize(0);

        Queries queries3 = HttpRequestUtils.parseQueries("/index.html");
        assertThat(queries3.getParams()).hasSize(0);
    }

    @Test
    public void parseQueries_invalid() {
        String targetToken = "/users/create?userId=javajigi";
        Queries queries = HttpRequestUtils.parseQueries(targetToken);
        assertThat(queries.getValue("userId")).isEqualTo("javajigi");
        assertThatThrownBy(() -> queries.getValue("password"))
                .isInstanceOf(InvalidParameterKeyException.class);
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
