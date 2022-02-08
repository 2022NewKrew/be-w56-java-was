package http;

import http.body.ResponseBody;
import http.header.Cookie;
import http.header.Headers;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class ResponseBodyTest {

    @Test
    void createResponse() {
        ResponseBody responseBody = new ResponseBody();
        assertThat(responseBody.getBytes()).isEqualTo(new byte[]{});
    }

    @Test
    void createResponseHeader() {
        ResponseBody responseBody = new ResponseBody(new byte[]{1, 1, 1, 1});

        Map<String, String> map = new HashMap<>();
        map.put("Content-Type", "text/html;charset=utf-8");
        map.put("Content-Length", "4");
        Headers headers = Headers.create(map);

        assertThat(responseBody.createResponseHeader().getHeaders()).isEqualTo(headers.getHeaders());
    }

    @Test
    void createResponseHeaderWithUrlAndCookie() {
        ResponseBody responseBody = new ResponseBody(new byte[]{1, 1, 1, 1});

        Cookie cookie = new Cookie("logined", "true");

        Map<String, String> map = new HashMap<>();
        map.put("Content-Length", "4");
        map.put("Location", "/");
        map.put("Set-Cookie", cookie.createHeader());
        Headers headers = Headers.create(map);

        assertThat(responseBody.createResponseHeader("/", cookie).getHeaders()).isEqualTo(headers.getHeaders());
    }
}
