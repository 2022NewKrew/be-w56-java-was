package request;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RequestBody {

    private static final Logger LOG = LoggerFactory.getLogger(RequestBody.class);

    private final Map<String, String> bodyMap;

    public RequestBody() {
        this.bodyMap = new HashMap<>();
    }

    private RequestBody(Map<String, String> bodyMap) {
        this.bodyMap = bodyMap;
    }

    public static RequestBody of(BufferedReader br) {
        Map<String, String> body = new HashMap<>();
        try {
            String line;
            while (!(line = br.readLine()).equals("")) {
                LOG.debug(">> {}", line);
            }
        } catch (IOException e) {
            LOG.error(e.getMessage());
        }
        return new RequestBody(body);
    }

}
