package http;

import enums.HttpMethod;
import enums.HttpStatusCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class HttpResponseSender {

    private final HttpResponse httpResponse;
    private final DataOutputStream dos;
    private static final Logger log = LoggerFactory.getLogger(HttpResponseSender.class);

    public HttpResponseSender(HttpResponse httpResponse, OutputStream out) {
        this.httpResponse = httpResponse;
        dos = new DataOutputStream(out);
    }

    public void sendResponse() {
        if (httpResponse.getStatusCode().equals(HttpStatusCode._200)) {
            sendResponse200();
        }
        else if (httpResponse.getStatusCode().equals(HttpStatusCode._302)) {
            sendResponse302();
        }
    }

    public void sendResponse200() {
        response200Header();
        responseBody();
    }

    public void sendResponse302() {
        response302Header(httpResponse.getRedirectUrl());
    }

    public void sendResponseLogin(boolean validLogin) {
        responseLoginHeader(validLogin);
    }

    public void response200Header() {
        try {
            dos.writeBytes(httpResponse.getProtocol().getName() + " " + httpResponse.getStatusCode().getMessage() + " \r\n");
            dos.writeBytes("Content-Type: " + httpResponse.getResponseContentType() + ";charset=utf-8\r\n");
            dos.writeBytes("Content-Length: " + httpResponse.getBody().length + "\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    public void responseLoginHeader(boolean validLogin) {
        try {
            dos.writeBytes("HTTP/1.1 302 Found \r\n");
            if (validLogin) {
                dos.writeBytes("Location: /index.html \r\n");
                dos.writeBytes("Set-Cookie: logined=true; Path=/ \r\n");
            }
            else {
                dos.writeBytes("Location: /user/login_failed.html \r\n");
                dos.writeBytes("Set-Cookie: logined=false; Path=/ \r\n");
            }
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    public void response302Header(String redirectUrl) {
        try {
            dos.writeBytes("HTTP/1.1 302 Found \r\n");
            dos.writeBytes("Location: " + redirectUrl);
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    public void responseBody() {
        try {
            dos.write(httpResponse.getBody(), 0, httpResponse.getBody().length);
            dos.flush();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

}
