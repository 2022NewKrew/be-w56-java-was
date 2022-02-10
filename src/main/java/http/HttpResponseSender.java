package http;

import enums.HttpMethod;
import enums.HttpStatusCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpResponseUtils;

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

    private void sendResponse200() {
        response200Header();
        responseBody();
    }

    private void sendResponse302() {
        response302Header(httpResponse.getRedirectUrl());
    }

    private void response200Header() {
        try {
            dos.writeBytes(httpResponse.getProtocol().getName() + " " + httpResponse.getStatusCode().getMessage() + " \r\n");
            dos.writeBytes("Content-Type: " + httpResponse.getResponseContentType() + ";charset=utf-8\r\n");
            dos.writeBytes("Content-Length: " + httpResponse.getBody().length + "\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void response302Header(String redirectUrl) {
        try {
            dos.writeBytes("HTTP/1.1 302 Found \r\n");
            dos.writeBytes("Location: " + redirectUrl+ "\r\n");
            if (httpResponse.getCookie() != null) {
                System.out.println(HttpResponseUtils.cookieString(httpResponse.getCookie()));
                dos.writeBytes("Set-Cookie: " + HttpResponseUtils.cookieString(httpResponse.getCookie()) + " \r\n");
            }
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void responseBody() {
        try {
            dos.write(httpResponse.getBody(), 0, httpResponse.getBody().length);
            dos.flush();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

}
