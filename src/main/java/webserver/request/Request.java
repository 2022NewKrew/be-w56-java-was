package webserver.request;

import java.util.Map;
import lombok.Builder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.MultiValueMap;

@Builder
public class Request {

    private static final Logger log = LoggerFactory.getLogger(Request.class);

    private final HttpMethod method;
    private final String uri;
    private final MultiValueMap<String, String> headers;
    private final Map<String, String> body;

    public HttpMethod getMethod() {
        return method;
    }

    public String getUri() {
        return uri;
    }

    public String getParam(String key) {
        return body.get(key);
    }
}
