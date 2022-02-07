package request;

import java.io.BufferedReader;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RequestBody {

    private static final Logger LOG = LoggerFactory.getLogger(RequestBody.class);

    private final char[] body;

    private RequestBody(char[] body) {
        this.body = body;
    }

    public static RequestBody of(BufferedReader br, Integer contentLength) {
        char[] body = new char[contentLength];
        try {
            br.read(body, 0, contentLength);
        } catch (IOException e) {
            LOG.error(e.getMessage());
        }
        return new RequestBody(body);
    }

    public char[] getBody() {
        return body;
    }
}
