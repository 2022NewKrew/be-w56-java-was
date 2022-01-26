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

    public byte[] getBody() {
        return body;
    }

    public void setBody(String url) throws IOException {
        body = Files.readAllBytes(new File("./webapp" + url).toPath());
    }

    public void response200Header(DataOutputStream dos, String url, int lengthOfBodyContent) {
        String responseContentType = contentTypeFromUrl(url);
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

    private String contentTypeFromUrl(String url) {
        if (url.equals("/")) {
            return "*/*";
        }
        String[] splitUrl = url.split("\\.");
        String extension = splitUrl[splitUrl.length-1];
        if (extension.equals("html")) {
            return "text/html";
        }
        if (extension.equals("css")) {
            return "text/css";
        }
        if (extension.equals("js")) {
            return "application/javascript";
        }
        if (extension.equals("ico")) {
            return "image/x-icon";
        }
        return "*/*";
    }


}
