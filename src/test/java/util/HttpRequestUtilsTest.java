package util;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import util.HttpRequestUtils.Pair;

import java.util.Map;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class HttpRequestUtilsTest {
    @ParameterizedTest
    @MethodSource("parseQueryStringParams")
    public void parseQueryString(String queryString, String userIdExpected, String passwordExpected) {
        Map<String, String> parameters = HttpRequestUtils.parseQueryString(queryString);

        assertAll(
                () -> assertEquals(userIdExpected, parameters.get("userId")),
                () -> assertEquals(passwordExpected, parameters.get("password"))
        );
    }

    static Stream<Arguments> parseQueryStringParams() {
        return Stream.of(
                Arguments.arguments("userId=javajigi", "javajigi", null),
                Arguments.arguments("userId=javajigi&password=password2", "javajigi", "password2"),
                Arguments.arguments("userId=javajigi&password", "javajigi", null)
        );
    }

    @ParameterizedTest
    @MethodSource("parseQueryStringNullParams")
    public void parseQueryString_null(String null_) {
        Map<String, String> parameters = HttpRequestUtils.parseQueryString(null_);
        assertTrue(parameters.isEmpty());
    }

    static Stream<Arguments> parseQueryStringNullParams() {
        return Stream.of(
                Arguments.arguments((String) null),
                Arguments.arguments(""),
                Arguments.arguments(" ")
        );
    }

    @Test
    public void parseCookies() {
        String cookies = "logined=true; JSessionId=1234";
        Map<String, String> parameters = HttpRequestUtils.parseCookies(cookies);

        assertEquals(parameters.get("logined"), "true");
        assertEquals(parameters.get("JSessionId"), "1234");
        assertNull(parameters.get("session"));
    }

    @ParameterizedTest
    @MethodSource("getKeyValueParams")
    public void getKeyValue(String keyValue, Pair expected) throws Exception {
        Pair pair = HttpRequestUtils.getKeyValue(keyValue, "=");
        assertEquals(expected, pair);
    }

    static Stream<Arguments> getKeyValueParams() {
        return Stream.of(
                Arguments.arguments("userId=javajigi", new Pair("userId", "javajigi")),
                Arguments.arguments("userId", null)
        );
    }

    @Test
    public void parseHeader() throws Exception {
        String header = "Content-Length: 59";
        Pair pair = HttpRequestUtils.parseHeader(header);
        assertEquals(new Pair("Content-Length", "59"), pair);
    }

    @ParameterizedTest
    @CsvSource({
            "GET /index.html HTTP/1.1,/index.html",
            "POST /member/memberForm.html HTTP/1.1,/member/memberForm.html"
    })
    public void parseRequestLine(String requestLine, String expected) {
        String[] token = HttpRequestUtils.parseRequestLine(requestLine);
        assertEquals(expected, token[1]);
    }
}
