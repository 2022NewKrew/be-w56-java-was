package http;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class HttpResponse {

    private DataOutputStream dos;

    public HttpResponse(OutputStream out) {
        this.dos = new DataOutputStream(out);
    }

    public void response200Header(String contentType, int lengthOfBodyContent) {
        System.out.println("contentType = " + contentType);
        try {
            this.dos.writeBytes("HTTP/1.1 200 OK \r\n");
            this.dos.writeBytes("Content-Type: " + contentType + ";");
            this.dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
            this.dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    public void responseBody(byte[] body) {
        try {
            this.dos.write(body, 0, body.length);
            this.dos.flush();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}
