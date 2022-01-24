package webserver.response;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.IIOException;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpRequestUtils.Pair;
import webserver.RequestHandler;

@Setter
public class Response {
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);
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

    public static Response createErrorResponse(Socket connection, StatusCode statusCode, String message) {
        try {
            Response response = new Response(connection.getOutputStream());
            response.statusCode = statusCode;
            response.setContents("text/plain;charset=utf-8", message.getBytes(StandardCharsets.UTF_8));
            return response;
        } catch (IOException e) {
            log.error(e.getMessage());
            return new Response(null);
        }
    }

    public void addHeader(Pair header) {
        headers.add(header);
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
            log.error(e.getMessage());
        }
    }

    private void writeHeaders(DataOutputStream dos) throws IOException {
        // dos = new DataOutputStream(System.out);
        //System.out.println(version);
        //System.out.println(statusCode.getDescription());
        dos.writeBytes(version + " " + statusCode.getStatus() + " " + statusCode.getDescription() + "\r\n");
        for (Pair header : headers) {
            dos.writeBytes(header.getKey() + ": " + header.getValue() + "\r\n");
        }
        dos.writeBytes("\r\n");
    }

    private void writeBody(DataOutputStream dos) throws IOException {
        //dos = new DataOutputStream(System.out);

        if (body.length > 0) {
            dos.write(body, 0, body.length);
            dos.flush();
        }
    }
}
