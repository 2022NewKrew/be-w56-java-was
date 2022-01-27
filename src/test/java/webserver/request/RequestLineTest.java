package webserver.request;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class RequestLineTest {

    @Test
    @DisplayName("올바른 request line")
    void test1() {
        String inputRequestLine = "GET /background.png HTTP/1.0";
        RequestLine requestLine = RequestLine.from(inputRequestLine);

        assertThat(requestLine.getHttpRequestMethod()).isEqualTo(HttpRequestMethod.GET);
        assertThat(requestLine.getRequestUri()).isEqualTo("/background.png");
        assertThat(requestLine.getHttpVersion()).isEqualTo("HTTP/1.0");
    }

    @Test
    @DisplayName("잘못된 request line 입력시 에러")
    void test2() {
        String inputRequestLine = "POST HTTP/1.0";

        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(
                () -> RequestLine.from(inputRequestLine)
        );
    }
}
