package http;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;

public class HttpResponse {

    private byte[] body;
    private String responseContentType;
    private DataOutputStream dos;
    private static final Logger log = LoggerFactory.getLogger(HttpResponse.class);

    public void sendResponse() {
        response200Header();
        responseBody();
    }

    public byte[] getBody() {
        return body;
    }

    public void setBody(String responseDataPath) throws IOException {
        body = Files.readAllBytes(new File("./webapp" + responseDataPath).toPath());
    }

    public void setDos(OutputStream out) {
        dos = new DataOutputStream(out);
    }

    public void setResponseContentType(String responseDataPath) {
        responseContentType = contentTypeFromPath(responseDataPath);
    }

    public void response200Header() {
        int lengthOfBodyContent = body.length;
        try {
            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            dos.writeBytes("Content-Type: " + responseContentType + ";charset=utf-8\r\n");
            dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    public void responseBody() {
        try {
            dos.write(body, 0, body.length);
            dos.flush();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    String contentTypeFromPath(String responseDataPath) {
        String[] splitPath = responseDataPath.split("\\.");
        if (splitPath.length == 1) {
            return "text/html";
        }
        String extension = splitPath[splitPath.length-1];
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
        return "application/octet-stream";
    }


}
