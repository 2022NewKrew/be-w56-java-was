package webserver.response;

import com.google.common.collect.Maps;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Setter
public class Response {

    private static final Logger log = LoggerFactory.getLogger(Response.class);
    private final OutputStream out;

    private String version = "HTTP/1.1";
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
        response.version = "HTTP/1.1";
        response.statusCode = statusCode;
        response.setContents("text/plain;charset=utf-8", message.getBytes(StandardCharsets.UTF_8));
        return response;
    }

    public void setContents(String contentType, byte[] content) {
        headers.put("Content-Length", String.valueOf(content.length));
        headers.put("Content-Type", contentType);
        body = content;
    }

    public void write() {
        try {
            DataOutputStream dos = new DataOutputStream(out);
            writeHeaders(dos);
            writeBody(dos);
        } catch (IOException e) {
            log.error(e.getMessage());
            e.printStackTrace();
        }
    }

    private void writeHeaders(DataOutputStream dos) throws IOException {
        dos.writeBytes(
            version + " " + statusCode.getStatus() + " " + statusCode.getDescription() + "\r\n");
        log.debug("http response line: {}",
            version + " " + statusCode.getStatus() + " " + statusCode.getDescription());
        for (String key : headers.keySet()) {
            dos.writeBytes(key + ": " + headers.get(key) + "\r\n");
            log.debug("http header line: {}", key + ": " + headers.get(key));
        }
        dos.writeBytes("\r\n");
    }

    private void writeBody(DataOutputStream dos) throws IOException {
        if (body.length > 0) {
            dos.write(body, 0, body.length);
            //log.debug("http response body: {}", body);
            dos.flush();
        }
    }

    public void redirectTo(String uri) {
        statusCode = StatusCode.FOUND;
        headers.put("Location", HOST + uri);
    }
}
