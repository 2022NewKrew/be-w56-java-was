package http;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class RequestMessageTest {

    @Test
    void createRequestMessageFailed_WhenNull() {
        assertThatThrownBy(() -> new RequestMessage(null, null, null))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Disabled
    @Test
    void readStaticFileFailed_WhenWrongPath() {
        RequestLine requestLine = RequestLine.create("GET / HTTP/1.0");
        Map<String, String> map = new HashMap<>();
        map.put("Host", "localhost:8080");
        map.put("Connection", "keep-alive");
        map.put("Cache-Control", "max-age=0");
        Headers headers = Headers.create(map);
        RequestMessage requestMessage = new RequestMessage(requestLine, headers, null);

//        assertThatThrownBy(requestMessage::readStaticFile)
//                .isInstanceOf(IOException.class);
    }

    @Disabled
    @Test
    void readStaticFileSuccess() {
        RequestLine requestLine = RequestLine.create("GET /index.html HTTP/1.0");
        Map<String, String> map = new HashMap<>();
        map.put("Host", "localhost:8080");
        map.put("Connection", "keep-alive");
        map.put("Cache-Control", "max-age=0");
        Headers headers = Headers.create(map);
        RequestMessage requestMessage = new RequestMessage(requestLine, headers, null);

//        requestMessage.readStaticFile();
    }
}
