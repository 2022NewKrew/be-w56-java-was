package webserver.response;

import java.util.List;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@Getter
public class Response {

    private static final Logger log = LoggerFactory.getLogger(Response.class);
    private static final String HTTP_VERSION = "HTTP/1.1";

    private final StatusCode statusCode;
    private final MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
    private byte[] body;

    protected Response(StatusCode statusCode) {
        this.statusCode = statusCode;
    }

    protected void addHeader(String key, String value) {
        headers.add(key, value);
    }

    protected void setContents(String contentType, byte[] content) {
        headers.put("Content-Length", List.of(String.valueOf(content.length)));
        headers.put("Content-Type", List.of(contentType));
        body = content;
    }

    public Response setCookie(String... args) {
        addHeader("Set-Cookie", String.join("; ", args));
        return this;
    }
}
