package util;

import model.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.*;

class HttpHeaderUtilsTest {

    @Test
    void getHttpRequestUrl() {
        String result = HttpHeaderUtils.getHttpRequestUrl("GET /index.html HTTP/1.1");
        assertThat(result).isEqualTo("/index.html");
    }

    @Test
    void getUserInfoFromUrl() {
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

    private static Stream<Arguments> parseRequestLine() {
        return Stream.of(
                Arguments.of(
                        "GET /user/create?userId=javajigi&password=password&name=%EB%B0%95%EC%9E%AC%EC%84%B1&email=javajigi%40slipp.net HTTP/1.1",
                        Arrays.asList("GET", "/user/create", "userId=javajigi&password=password&name=%EB%B0%95%EC%9E%AC%EC%84%B1&email=javajigi%40slipp.net", "HTTP/1.1")
                ),
                Arguments.of("GET /index.html HTTP/1.1", Arrays.asList("GET", "/index.html", "", "HTTP/1.1")),
                Arguments.of("POST /user/create HTTP/1.1", Arrays.asList("POST", "/user/create", "", "HTTP/1.1"))
        );
    }

    @ParameterizedTest
    @MethodSource("parseRequestLine")
    void parseRequestLine(String input, List<String> expected) {
        List<String> result = HttpHeaderUtils.parseRequestLine(input);
        assertThat(result).isEqualTo(expected);
    }
}
