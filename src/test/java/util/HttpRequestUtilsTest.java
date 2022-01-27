package util;

import static org.assertj.core.api.Assertions.*;

import java.util.List;
import java.util.Map;


import org.junit.jupiter.api.Test;
import util.HttpRequestUtils.Pair;

public class HttpRequestUtilsTest {
    @Test
    public void parseQueryString() {
        String queryString = "userId=javajigi";
        Map<String, List<String>> parameters = HttpRequestUtils.parseQueryString(queryString);
        assertThat(parameters.get("userId").get(0)).isEqualTo("javajigi");
        assertThat(parameters.get("password")).isNull();

        queryString = "userId=javajigi&password=password2";
        parameters = HttpRequestUtils.parseQueryString(queryString);
        assertThat(parameters.get("userId").get(0)).isEqualTo("javajigi");
        assertThat(parameters.get("password").get(0)).isEqualTo("password2");
    }

    @Test
    public void parseQueryString_multipleValue() {
        String queryString = "role=ADMIN&role=USER";
        Map<String, List<String>> parameters = HttpRequestUtils.parseQueryString(queryString);
        assertThat(parameters.size()).isEqualTo(1);
        assertThat(parameters.get("role").size()).isEqualTo(2);
        assertThat(parameters.get("role")).containsExactly("ADMIN", "USER");
    }

    @Test
    public void parseQueryString_null() {
        Map<String, List<String>> parameters = HttpRequestUtils.parseQueryString(null);
        assertThat(parameters.isEmpty()).isTrue();

        parameters = HttpRequestUtils.parseQueryString("");
        assertThat(parameters.isEmpty()).isTrue();

        parameters = HttpRequestUtils.parseQueryString(" ");
        assertThat(parameters.isEmpty()).isTrue();
    }

    @Test
    public void parseQueryString_invalid() {
        String queryString = "userId=javajigi&password";
        Map<String, List<String>> parameters = HttpRequestUtils.parseQueryString(queryString);
        assertThat(parameters.get("userId").get(0)).isEqualTo("javajigi");
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
}
