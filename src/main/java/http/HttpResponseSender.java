package http;

import enums.HttpMethod;
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

    public void sendResponse(HttpMethod httpMethod, String url) {
        if (httpMethod.equals(HttpMethod.POST)) {
            response302Header(url);
            return;
        }
        response200Header();
        responseBody();
    }

    public void response200Header() {
        try {
            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            dos.writeBytes("Content-Type: " + httpResponse.getResponseContentType() + ";charset=utf-8\r\n");
            dos.writeBytes("Content-Length: " + httpResponse.getBody().length + "\r\n");
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
