package http;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class HttpResponse {

    private byte[] body;

    private static final Logger log = LoggerFactory.getLogger(HttpResponse.class);
    public void response200Header(DataOutputStream dos, int lengthOfBodyContent, String responseContentType) {
        try {
            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            dos.writeBytes("Content-Type: " + responseContentType + ";charset=utf-8\r\n");
            dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    public void responseBody(DataOutputStream dos, byte[] body) {
        try {
            dos.write(body, 0, body.length);
            dos.flush();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    public byte[] getBody() {
        return body;
    }

    public void setBody(String url) throws IOException {
        body = Files.readAllBytes(new File("./webapp" + url).toPath());
    }

}
