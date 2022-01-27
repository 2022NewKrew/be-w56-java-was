package webserver.response;

import com.google.common.collect.Maps;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Getter
public class Response {

    private static final Logger log = LoggerFactory.getLogger(Response.class);
    private final OutputStream out;

    private static final String HTTP_VERSION = "HTTP/1.1";
    private StatusCode statusCode;
    private Map<String, String> headers = Maps.newHashMap();
    private byte[] body;

    private static final String HOST = "http://localhost:8080";

    private Response(OutputStream out) {
        this.out = out;
    }

    public static Response create(OutputStream out) {
        return new Response(out);
    }

    public static Response createErrorResponse(OutputStream out, StatusCode statusCode,
        String message) {
        Response response = new Response(out);
        response.statusCode = statusCode;
        response.setContents("text/plain;charset=utf-8", message.getBytes(StandardCharsets.UTF_8));
        return response;
    }

    public void setStatusCode(StatusCode statusCode) {
        this.statusCode = statusCode;
    }

    public void setContents(String contentType, byte[] content) {
        headers.put("Content-Length", String.valueOf(content.length));
        headers.put("Content-Type", contentType);
        body = content;
    }

    public void redirectTo(String uri) {
        statusCode = StatusCode.FOUND;
        headers.put("Location", HOST + uri);
    }
}
