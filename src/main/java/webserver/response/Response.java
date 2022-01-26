package webserver.response;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpRequestUtils.Pair;

@Setter
public class Response {

    private static final Logger log = LoggerFactory.getLogger(Response.class);
    private final OutputStream out;

    private String version;
    private StatusCode statusCode;
    private List<Pair> headers = new ArrayList<>();
    private byte[] body;

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
        headers.add(new Pair("Content-Length", String.valueOf(content.length)));
        headers.add(new Pair("Content-Type", contentType));
        body = content;
    }

    public void write() {
        try {
            DataOutputStream dos = new DataOutputStream(out);
            writeHeaders(dos);
            writeBody(dos);
        } catch (IOException e) {
            log.error("IOException in write func: {}", e.getMessage());
        }
    }

    private void writeHeaders(DataOutputStream dos) throws IOException {
        dos.writeBytes(
            version + " " + statusCode.getStatus() + " " + statusCode.getDescription() + "\r\n");
        for (Pair header : headers) {
            dos.writeBytes(header.getKey() + ": " + header.getValue() + "\r\n");
        }
        dos.writeBytes("\r\n");
    }

    private void writeBody(DataOutputStream dos) throws IOException {
        if (body.length > 0) {
            dos.write(body, 0, body.length);
            dos.flush();
        }
    }
}
