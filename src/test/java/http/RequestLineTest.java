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
        new RequestLine(HttpMethod.GET, new RequestTarget(new Path("/index.html")), HttpVersion.V_1_0);
    }

//    @Test
//    void findStaticFileSuccess() {
//        RequestLine requestLine = new RequestLine(HttpMethod.GET, new RequestTarget(new Path("/index.html")), HttpVersion.V_1_0);
//        requestLine.findStaticFile();
//    }
}
