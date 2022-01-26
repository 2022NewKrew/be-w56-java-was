package model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.crypto.Data;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class Response {
    private final Logger logger = LoggerFactory.getLogger(Response.class);

    private byte[] body;
    private final DataOutputStream dos;
    private String url;

    public Response(DataOutputStream dos) {
        this.dos = dos;
    }

    public void buildBody(Request request) throws IOException {
        this.url = request.getRequestHeader().getRequestLine().getUrl();
        this.body = Files.readAllBytes(
                new File("webapp/" + url).toPath());
    }

    public void buildBody(String url) throws IOException {
        this.url = url;
        this.body = Files.readAllBytes(
                new File("webapp/" + this.url).toPath());
    }

    public void build302Response() {
        response302Header();
        flushResponseBody();
    }

    private void response302Header() {
        try {
            dos.writeBytes("HTTP/1.1 302 Found \r\n");
            dos.writeBytes("Location: /index.html");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    public void build200Response() {
        response200Header();
        flushResponseBody();
    }

    private void response200Header() {
        try {
            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            dos.writeBytes(getContentType());
            dos.writeBytes("Content-Length: " + body.length + "\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private String getContentType() {
        if (url.endsWith("html")) {
            return "Content-Type: text/html;charset=utf-8\r\n";
        } else if (url.endsWith("js")) {
            return "Content-Type: application/javascript;charset=utf-8\r\n";
        } else if (url.endsWith("css")) {
            return "Content-Type: text/css;charset=utf-8\r\n";
        } else if (url.endsWith("ico")) {
            return "Content-Type: image/x-icon;charset=utf-8\r\n";
        } else if (url.endsWith("ttf")) {
            return "Content-Type: font/ttf;charset=utf-8\r\n";
        } else if (url.endsWith("woff")) {
            return "Content-Type: font/woff;charset=utf-8\r\n";
        } else {
            return null;
        }
    }

    private void flushResponseBody() {
        try {
            dos.write(body, 0, body.length);
            dos.flush();
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }
}
