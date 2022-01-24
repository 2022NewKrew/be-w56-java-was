package http.request;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class HttpRequestLineTest {

    @Test
    @DisplayName("HttpRequestLine 정상 동작 테스트")
    public void httpRequestLineBasicTest() {
        String line = "GET /index.html?name=carrot&age=17 HTTP/1.1";
        HttpRequestLine httpRequestLine = new HttpRequestLine(line);

        assertThat(httpRequestLine.getMethod()).isEqualTo("GET");
        assertThat(httpRequestLine.getUrl()).isEqualTo("/index.html");
        assertThat(httpRequestLine.getVersion()).isEqualTo("HTTP/1.1");
        assertThat(httpRequestLine.getParam("name")).isEqualTo("carrot");
        assertThat(httpRequestLine.getParam("age")).isEqualTo("17");
    }
}
