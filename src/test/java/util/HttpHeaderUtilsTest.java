package util;

import model.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.UnsupportedEncodingException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

class HttpHeaderUtilsTest {

    @Test
    void getHttpRequestUrl() {
        String result = HttpHeaderUtils.getHttpRequestUrl("GET /index.html HTTP/1.1");
        assertThat(result).isEqualTo("/index.html");
    }

    @Test
    void getUserInfoFromUrl() throws UnsupportedEncodingException {
        var result = HttpHeaderUtils.getUserInfoFromUrl("userId=javajigi&password=password&name=%EB%B0%95%EC%9E%AC%EC%84%B1&email=javajigi%40slipp.net");
        assertThat(result).isEqualTo(new User("javajigi", "password", "박재성", "javajigi@slipp.net"));
    }

    @Test
    void getQuery() {
        Optional<String> result = HttpHeaderUtils.getQuery("/create?userId=javajigi&password=password&name=%EB%B0%95%EC%9E%AC%EC%84%B1&email=javajigi%40slipp.net");
        assertThat(result).isEqualTo(Optional.of("userId=javajigi&password=password&name=%EB%B0%95%EC%9E%AC%EC%84%B1&email=javajigi%40slipp.net"));
    }

    @ParameterizedTest
    @CsvSource({"/create?userId=javajigi&password=password&name=%EB%B0%95%EC%9E%AC%EC%84%B1&email=javajigi%40slipp.net,/create",
            "/index.html,/index.html",
            "/user/form.html,/user/form.html"})
    void getUrl(String input, String expected) {
        String result = HttpHeaderUtils.getUrl(input);
        assertThat(result).isEqualTo(expected);
    }

    @ParameterizedTest
    @CsvSource({"/index.html,text/html",
            "/js/jquery-2.2.0.min.js,text/javascript",
            "/css/styles.css,text/css"})
    void getContentTypeFromUrl(String input, String expected) {
        String result = HttpHeaderUtils.getContentTypeFromUrl(input);
        assertThat(result).isEqualTo(expected);
    }
}
