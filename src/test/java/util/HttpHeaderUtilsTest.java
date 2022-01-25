package util;

import model.User;
import org.junit.jupiter.api.Test;

import java.io.UnsupportedEncodingException;

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
}
