package http;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class RequestLineTest {

    @Test
    void createRequestStartLineFailed() {
        assertThatThrownBy(() -> new RequestLine(null, null, null))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void createRequestStartLineSuccess() {
        new RequestLine(HttpMethod.GET, new RequestTarget("/index.html"), HttpVersion.V_1_0);
    }
}
