package http;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class RequestMessageTest {

    @Test
    void createRequestMessageFailed_WhenNull() {
        assertThatThrownBy(() -> new RequestMessage(null, null))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void readStaticFileFailed_WhenWrongPath() {
        RequestLine requestLine = new RequestLine(HttpMethod.GET, new RequestTarget("/abc"), HttpVersion.V_1_0);
        Map<String, String> map = new HashMap<>();
        map.put("Host", "localhost:8080");
        map.put("Connection", "keep-alive");
        map.put("Cache-Control", "max-age=0");
        Headers headers = Headers.create(map);
        RequestMessage requestMessage = new RequestMessage(requestLine, headers);

        assertThatThrownBy(requestMessage::readStaticFile)
                .isInstanceOf(IOException.class);
    }

    @Test
    void readStaticFileSuccess() throws IOException {
        RequestLine requestLine = new RequestLine(HttpMethod.GET, new RequestTarget("/index.html"), HttpVersion.V_1_0);
        Map<String, String> map = new HashMap<>();
        map.put("Host", "localhost:8080");
        map.put("Connection", "keep-alive");
        map.put("Cache-Control", "max-age=0");
        Headers headers = Headers.create(map);
        RequestMessage requestMessage = new RequestMessage(requestLine, headers);

        requestMessage.readStaticFile();
    }
}
