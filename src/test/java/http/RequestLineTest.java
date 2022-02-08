package http;

import http.startline.RequestLine;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class RequestLineTest {

    @Test
    void createRequestStartLineFailed() {
        assertThatThrownBy(() -> RequestLine.create(""))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void createRequestStartLineSuccess() {
        String input = "GET / HTTP/1.1";
        RequestLine.create(input);
    }

    @Disabled
    @Test
    void findStaticFileSuccess() {
        String input = "GET /index.html HTTP/1.1";
        RequestLine requestLine = RequestLine.create(input);
        requestLine.createStaticFile();
    }
}
