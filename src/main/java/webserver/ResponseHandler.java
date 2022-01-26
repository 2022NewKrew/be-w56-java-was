package webserver;

import enums.HttpStatus;
import org.apache.tika.Tika;
import org.slf4j.Logger;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;

public class ResponseHandler {

    private DataOutputStream dos;
    private Logger log;

    public ResponseHandler(OutputStream out, Logger log) {
        dos = new DataOutputStream(out);
        this.log = log;
    }

    public void response(String url, HttpStatus httpStatus, String cookie) {
        try {
            String status = httpStatus.getStatus();
            dos.writeBytes("HTTP/1.1 " + status + " \r\n");
            responseEachStatus(url, httpStatus, cookie);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void responseEachStatus(String url, HttpStatus httpStatus, String cookie) throws IOException {
        switch (httpStatus) {
            case OK:
                response200(url);
                break;
            case FOUND:
                response302(cookie, url);
                break;
        }
    }

    private void response302(String cookie, String url) throws IOException {
        dos.writeBytes("Location: " + url + "\r\n");
        if(cookie != null)
            dos.writeBytes("Set-Cookie: " + cookie + "; Path=/");
        dos.writeBytes("\r\n");
    }

    private void response200(String url) throws IOException {
        File file = new File("./webapp" + url);
        String contentType = new Tika().detect(file);
        byte[] body = Files.readAllBytes(file.toPath());
        response200Header(body.length, contentType);
        responseBody(dos, body);
    }

    private void response200Header(int lengthOfBodyContent, String contentType) throws IOException {
        dos.writeBytes("Content-Type: " + contentType + ";charset=utf-8\r\n");
        dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
        dos.writeBytes("\r\n");
    }

    private void responseBody(DataOutputStream dos, byte[] body) throws IOException {
        dos.write(body, 0, body.length);
        dos.flush();
    }

}
