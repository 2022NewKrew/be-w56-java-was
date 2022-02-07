package http;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.RequestHandler;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Map;

public class HttpResponse {

    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

    private final HttpHeader httpHeader = new HttpHeader();
    private final String NOT_FOUND_MESSAGE = "페이지를 찾을 수 없습니다.";
    private final DataOutputStream dos;

    public HttpResponse(OutputStream out) throws IOException {
        dos = new DataOutputStream(out);
    }

    public HttpHeader getHttpHeader() {
        return httpHeader;
    }

    public void forward(String url) {
        byte[] body;
        try {
            body = Files.readAllBytes(new File("./webapp" + url).toPath());
            response200Header(body.length);
        } catch (IOException e) {
            body = NOT_FOUND_MESSAGE.getBytes(StandardCharsets.UTF_8);
            response404Header(body.length);
        }
        responseBody(body);
    }

    public void forwardBody(String body) {
        byte[] contents = body.getBytes();
        response200Header(contents.length);
        responseBody(contents);
    }

    public void sendRedirect302Header(String redirectUrl) {
        try {
            dos.writeBytes("HTTP/1.1 302 FOUND \r\n");
            dos.writeBytes("Content-Type: text/html;charset=utf-8\r\n");
            writeHeaderParams();
            dos.writeBytes("Location: " + redirectUrl + " \r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }


    private void writeHeaderParams() {
        Map<String, String> header = httpHeader.getHeader();
        for (String key : header.keySet()) {
            try {
                dos.writeBytes(key + ": " + header.get(key) + " \r\n");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    private void response200Header(int lengthOfBodyContent) {
        try {
            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            dos.writeBytes("Content-Type: text/html;charset=utf-8\r\n");
            writeHeaderParams();
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

    private void response404Header(int lengthOfBodyContent) {
        try {
            dos.writeBytes("HTTP/1.1 404 Not Found \r\n");
            dos.writeBytes("Content-Type: text/html;charset=utf-8\r\n");
            writeHeaderParams();
            dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}
