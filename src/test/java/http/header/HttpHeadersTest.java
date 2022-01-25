package http.header;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class HttpHeadersTest {

    @Test
    @DisplayName("정상 동작 테스트")
    public void httpHeadersBasicTest() {
        List<String> strings = Arrays.asList("Accept: *", "User-Agent: Browser", "Content-Type: text/html");
        HttpHeaders httpHeaders = new HttpHeaders(strings);

        assertThat(httpHeaders.getFirst("Accept")).isEqualTo("*");
        assertThat(httpHeaders.getFirst("User-Agent")).isEqualTo("Browser");
        assertThat(httpHeaders.getFirst("Content-Type")).isEqualTo("text/html");
    }
}
