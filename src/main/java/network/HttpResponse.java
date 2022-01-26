package network;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HandlerMapper;

import java.io.DataOutputStream;
import java.io.IOException;

public class HttpResponse {

    private static final Logger log = LoggerFactory.getLogger(HttpResponse.class);
    private final DataOutputStream dos;
    private final HttpRequest httpRequest;

    public HttpResponse(DataOutputStream dos, HttpRequest httpRequest) {
        this.dos = dos;
        this.httpRequest = httpRequest;
        setResponse();
    }

    private void setResponse() {
        try {
            ResponseBody responseBody = HandlerMapper.requestMapping(httpRequest);
            byte[] body = responseBody.getBody();
            if (responseBody.getHttpStatus().equals(HttpStatus.FOUND)) {
                response302Header(body.length, httpRequest.getContentType(), responseBody.getLocation());
            } else {
                response200Header(body.length, httpRequest.getContentType());
            }
            responseBody(body);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void response200Header(int lengthOfBodyContent, String contentType) {
        try {
            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            dos.writeBytes("Content-Type: " + contentType + ";charset=utf-8\r\n");
            dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void response302Header(int lengthOfBodyContent, String contentType, String location) {
        try {
            dos.writeBytes("HTTP/1.1 302 Found \r\n");
            dos.writeBytes("Location: http://localhost:8080" + location + "\r\n");
            dos.writeBytes("Content-Type: " + contentType + ";charset=utf-8\r\n");
            dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void responseBody(byte[] body) {
        try {
            dos.write(body, 0, body.length);
            dos.flush();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}
