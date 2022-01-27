package webserver.http;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;

import lombok.extern.slf4j.Slf4j;

import webserver.util.Constant;

@Slf4j
public class HttpResponse {

    private DataOutputStream dos;

    public HttpResponse(OutputStream out) {
        this.dos = new DataOutputStream(out);
    }

    public void send(String path, String contentType) throws IOException {
        byte[] body = Files.readAllBytes(new File(Constant.ROOT_PATH + path).toPath());

        response200Header(contentType, body.length);
        responseBody(body);
    }

    public void sendRedirect(String path) {
        response302Header(path, false);
    }

    public void sendRedirectWithCookie(String path) {
        response302Header(path, true);
    }

    private void response200Header(String contentType, int lengthOfBodyContent) {
        try {
            this.dos.writeBytes("HTTP/1.1 200 OK \r\n");
            this.dos.writeBytes("Content-Type: " + contentType + ";\r\n");
            this.dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
            this.dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void response302Header(String path, boolean setCookie) {
        try {
            this.dos.writeBytes("HTTP/1.1 302 Found \r\n");
            this.dos.writeBytes("Location: " + path + " \r\n");
            if (setCookie) {
                this.dos.writeBytes("Set-Cookie: logined=true;/html; Path=/ \r\n");
            }
            this.dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void responseBody(byte[] body) {
        try {
            this.dos.write(body, 0, body.length);
            this.dos.flush();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}
