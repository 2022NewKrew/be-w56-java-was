package webserver.response;

import com.google.common.collect.Maps;
import java.util.Map;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Getter
public class Response {

    private static final Logger log = LoggerFactory.getLogger(Response.class);
    private static final String HTTP_VERSION = "HTTP/1.1";

    private final StatusCode statusCode;
    private final Map<String, String> headers = Maps.newHashMap();
    private byte[] body;

    protected Response(StatusCode statusCode) {
        this.statusCode = statusCode;
    }

    protected void addHeader(String key, String value) {
        headers.put(key, value);
    }

    protected void setContents(String contentType, byte[] content) {
        headers.put("Content-Length", String.valueOf(content.length));
        headers.put("Content-Type", contentType);
        body = content;
    }
}
