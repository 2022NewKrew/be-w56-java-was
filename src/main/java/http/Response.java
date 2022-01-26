package http;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;

public class Response {

    private static final Logger log = LoggerFactory.getLogger(Response.class);
    private final DataOutputStream dos;

    public Response(OutputStream out) {
        this.dos = new DataOutputStream(out);
    }

    public void staticResponse(String url) throws IOException {
        byte[] body = Files.readAllBytes(new File("./webapp" + url).toPath());
        String typeOfBodyContent = "text/html";
        if (url.contains(".css")) {
            typeOfBodyContent = "text/css";
        } else if (url.contains(".js")) {
            typeOfBodyContent = "application/javascript";
        }
        response200Header(typeOfBodyContent, body.length);
        responseBody(body);
    }

    public void redirectResponse(String location) {
        response302Header(location);
    }

    private void response200Header(String typeOfBodyContent, int lengthOfBodyContent) {
        try {
            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            dos.writeBytes("Content-Type: " + typeOfBodyContent + ";charset=utf-8\r\n");
            dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void response302Header(String location) {
        try {
            dos.writeBytes("HTTP/1.1 302 Found \r\n");
            dos.writeBytes("Location: http://localhost:8080" + location);
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void responseBody(byte[] body) {
        try {
            dos.write(body, 0, body.length);
            dos.flush();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}
