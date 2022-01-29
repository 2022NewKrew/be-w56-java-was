package util;

import http.*;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class HttpRequestParserTest {

    @Test
    void parseStartLineFailedWhenNull() {
        assertThatThrownBy(() -> HttpRequestParser.parseStartLine(""))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void parseStartLineSuccess() {
        String test1 = "GET /index.html HTTP/1.1";
        String test2 = "GET /index.html HTTP/1.0";
        String test3 = "GET /user/form.html HTTP/1.0";
        String test4 = "POST /user/form.html HTTP/1.0";

//        RequestLine expect1 = new RequestLine(HttpMethod.GET, new RequestTarget("/index.html"), HttpVersion.V_1_1);
//        RequestLine expect2 = new RequestLine(HttpMethod.GET, new RequestTarget("/index.html"), HttpVersion.V_1_0);
//        RequestLine expect3 = new RequestLine(HttpMethod.GET, new RequestTarget("/user/form.html"), HttpVersion.V_1_0);
//        RequestLine expect4 = new RequestLine(HttpMethod.POST, new RequestTarget("/user/form.html"), HttpVersion.V_1_0);

//        assertStartLine(test1, expect1);
//        assertStartLine(test2, expect2);
//        assertStartLine(test3, expect3);
//        assertStartLine(test4, expect4);
    }

    private void assertStartLine(String line, RequestLine expect) {
        RequestLine result = HttpRequestParser.parseStartLine(line);
        assertThat(result).isEqualTo(expect);
    }

    @Test
    void parseHeaderFailedWhenNull() {
        assertThatThrownBy(() -> HttpRequestParser.parseHeaders(Arrays.asList("")))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void parseHeaderSuccess() {
        List<String> test =  new ArrayList<>(Arrays.asList("Host: localhost:8080", "Connection: keep-alive", "Cache-Control: max-age=0"));

        Map<FieldName, FieldValue> map = new HashMap<>();
        map.put(new FieldName("Host"), new FieldValue("localhost:8080"));
        map.put(new FieldName("Connection"), new FieldValue("keep-alive"));
        map.put(new FieldName("Cache-Control"), new FieldValue("max-age=0"));

        Headers expect = new Headers(map);
        Headers result = HttpRequestParser.parseHeaders(test);

        assertThat(result).isEqualTo(expect);
    }

}
