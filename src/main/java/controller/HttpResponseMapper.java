package controller;

import http.response.HttpResponse;
import http.response.HttpResponseBody;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.DataOutputStream;
import java.io.IOException;

public class HttpResponseMapper {
    private static final Logger log = LoggerFactory.getLogger(HttpResponseMapper.class);

    public static void response200Header(DataOutputStream dos, HttpResponse httpResponse) {
        try {
            dos.writeBytes(httpResponse.getStatusLine().toString() +"\r\n");
            dos.writeBytes("Content-Length: " + httpResponse.getBody().getBodyLength() + "\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    public static void responseBody(DataOutputStream dos, HttpResponseBody body) {
        try {
            dos.write(body.getBody(), 0, body.getBodyLength());
            dos.flush();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}
