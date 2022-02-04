package util.util;

import org.junit.jupiter.api.Test;
import util.http.HttpRequestUtils;
import util.http.HttpRequestUtils.Pair;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class HttpRequestUtilsTest {
    @Test
    void parseQueryString() {
        String queryString = "userId=javajigi";
        Map<String, String> parameters = HttpRequestUtils.parseQueryString(queryString);
        assertThat(parameters).containsEntry("userId", "javajigi");
        assertThat(parameters.get("password")).isNull();
        queryString = "userId=javajigi&password=password2";
        parameters = HttpRequestUtils.parseQueryString(queryString);
        assertThat(parameters)
                .containsEntry("userId", "javajigi")
                .containsEntry("password", "password2");
    }

    @Test
    void parseQueryString_null() {
        Map<String, String> parameters = HttpRequestUtils.parseQueryString(null);
        assertThat(parameters).isEmpty();

        parameters = HttpRequestUtils.parseQueryString("");
        assertThat(parameters).isEmpty();

        parameters = HttpRequestUtils.parseQueryString(" ");
        assertThat(parameters).isEmpty();
    }

    @Test
    void parseQueryString_invalid() {
        String queryString = "userId=javajigi&password";
        Map<String, String> parameters = HttpRequestUtils.parseQueryString(queryString);
        assertThat(parameters).containsEntry("userId", "javajigi");
        assertThat(parameters.get("password")).isNull();
    }

    @Test
    void parseCookies() {
        String cookies = "logined=true; JSessionId=1234";
        Map<String, String> parameters = HttpRequestUtils.parseCookies(cookies);
        assertThat(parameters)
                .containsEntry("logined", "true")
                .containsEntry("JSessionId", "1234");
        assertThat(parameters.get("session")).isNull();
    }

    @Test
    void getKeyValue() throws Exception {
        Pair pair = HttpRequestUtils.getKeyValue("userId=javajigi", "=");
        assertThat(pair).isEqualTo(new Pair("userId", "javajigi"));
    }

    @Test
    void getKeyValue_invalid() throws Exception {
        Pair pair = HttpRequestUtils.getKeyValue("userId", "=");
        assertThat(pair).isNull();
    }

    @Test
    void parseHeader() throws Exception {
        String header = "Content-Length: 59";
        Pair pair = HttpRequestUtils.parseHeader(header);
        assertThat(pair).isEqualTo(new Pair("Content-Length", "59"));
    }



}
