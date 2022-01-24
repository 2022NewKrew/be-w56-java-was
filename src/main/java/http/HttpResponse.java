package http;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.DataOutputStream;
import java.io.IOException;

public class HttpResponse {

    private static final Logger log = LoggerFactory.getLogger(HttpResponse.class);

    private DataOutputStream dos;

    public HttpResponse(DataOutputStream dataOutputStream) {
        this.dos = dataOutputStream;
    }

    public void send(Resource resource) {
        writeHeader(resource);
        writeBody(resource.getContent());
    }

    private void writeHeader(Resource resource) {
        try {
            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            dos.writeBytes("Content-Type: " + resource.getType() + ";charset=utf-8\r\n");
            dos.writeBytes("Content-Length: " + resource.getContent().length + "\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void writeBody(byte[] body) {
        try {
            dos.write(body, 0, body.length);
            dos.flush();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}
