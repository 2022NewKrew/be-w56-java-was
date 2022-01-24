package http;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.RequestHandler;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

public class HttpResponse {

    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

    private final String NOT_FOUND_MESSAGE = "페이지를 찾을 수 없습니다.";
    private final DataOutputStream dos;

    public HttpResponse(OutputStream out) throws IOException {
        dos = new DataOutputStream(out);
    }

    public void getResponse(String url) throws IOException {
        byte[] body;
        try {
            body = Files.readAllBytes(new File("./webapp" + url).toPath());
            response200Header(dos, body.length);
        } catch (IOException e) {
            body = NOT_FOUND_MESSAGE.getBytes(StandardCharsets.UTF_8);
            response404Header(dos, body.length);
        }
        responseBody(dos, body);
    }

    private void response200Header(DataOutputStream dos, int lengthOfBodyContent) {
        try {
            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            dos.writeBytes("Content-Type: text/html;charset=utf-8\r\n");
            dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void responseBody(DataOutputStream dos, byte[] body) {
        try {
            dos.write(body, 0, body.length);
            dos.flush();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void response404Header(DataOutputStream dos, int lengthOfBodyContent) {
        try {
            dos.writeBytes("HTTP/1.1 404 Not Found \r\n");
            dos.writeBytes("Content-Type: text/html;charset=utf-8\r\n");
            dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}
