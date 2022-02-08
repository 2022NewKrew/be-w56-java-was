package http.request;

import java.io.BufferedReader;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpRequestBody {

    private static final Logger LOG = LoggerFactory.getLogger(HttpRequestBody.class);

    private final char[] body;

    private HttpRequestBody(char[] body) {
        this.body = body;
    }

    public static HttpRequestBody of(BufferedReader br, Integer contentLength) {
        char[] body = new char[contentLength];
        try {
            br.read(body, 0, contentLength);
        } catch (IOException e) {
            LOG.error(e.getMessage());
        }
        return new HttpRequestBody(body);
    }

    public char[] getBody() {
        return body;
    }
}
