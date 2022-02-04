package http.request;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class HttpRequestHeaderTest {
    @DisplayName("HttpRequestHeader header parsing 테스트")
    @Test
    void parseHeaderTest() {
        List<String> header = new ArrayList<>();
        header.add("Accept-Language: ko-KR");
        header.add("HOST: 127.0.0.1:8000");

        HttpRequestHeader requestHeader = HttpRequestHeader.parseHeader(header);

        Assertions.assertEquals("ko-KR", requestHeader.getIfPresent("Accept-Language"));
        Assertions.assertEquals("127.0.0.1:8000", requestHeader.getIfPresent("HOST"));
    }

    @DisplayName("HttpRequestHeader header parsing 테스트 (cookie)")
    @Test
    void parseHeaderCookieTest() {
        List<String> header = new ArrayList<>();
        header.add("Cookie: logined=true");

        HttpRequestHeader requestHeader = HttpRequestHeader.parseHeader(header);

        Assertions.assertEquals("true", requestHeader.getCookie("logined"));
        Assertions.assertEquals("", requestHeader.getCookie("invalidKey"));
    }
}