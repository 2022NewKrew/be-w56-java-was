package util;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class HttpHeaderUtilsTest {

    @Test
    void getHttpRequestUrl() {
        String result = HttpHeaderUtils.getHttpRequestUrl("GET /index.html HTTP/1.1");
        assertThat(result).isEqualTo("/index.html");
    }
}
