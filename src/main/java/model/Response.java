package model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.DataOutputStream;
import java.io.IOException;

public class Response {
    private final Logger logger = LoggerFactory.getLogger(Response.class);

    private byte[] body;
    private final DataOutputStream dos;

    public Response(DataOutputStream dos) {
        this.dos = dos;
    }

    public void build200Response(byte[] body, String extension) {
        this.body = body;
        response200Header(extension);
        flushResponseBody();
    }

    public void build302Response() {
        response302Header();
        flushWithoutBody();
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

    public void build302ResponseWithCookie(String redirectUrl, String cookieStatus) {
        response302HeaderWithCookie(redirectUrl, cookieStatus);
        flushWithoutBody();
    }

    private void response302HeaderWithCookie(String redirectUrl, String cookieStatus) {
        try {
            dos.writeBytes("HTTP/1.1 302 Found \r\n");
            dos.writeBytes("Location: " + redirectUrl + "\r\n");
            dos.writeBytes("Set-Cookie: logined=" + cookieStatus + "; Path=/\r\n");
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }


    private void response200Header(String extension) {
        try {
            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            dos.writeBytes(getContentType(extension));
            dos.writeBytes("Content-Length: " + body.length + "\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private String getContentType(String extension) {
        if (extension.equals("html")) {
            return "Content-Type: text/html;charset=utf-8\r\n";
        } else if (extension.equals("js")) {
            return "Content-Type: application/javascript;charset=utf-8\r\n";
        } else if (extension.equals("css")) {
            return "Content-Type: text/css;charset=utf-8\r\n";
        } else if (extension.equals("ico")) {
            return "Content-Type: image/x-icon;charset=utf-8\r\n";
        } else if (extension.equals("ttf")) {
            return "Content-Type: font/ttf;charset=utf-8\r\n";
        } else if (extension.equals("woff")) {
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

    private void flushWithoutBody() {
        try {
            dos.flush();
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }
}
