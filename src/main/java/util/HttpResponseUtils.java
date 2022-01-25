package util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.HttpResponse;
import webserver.RequestHandler;
import webserver.enums.MIME;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class HttpResponseUtils {
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

    public static void createResponse(OutputStream out, HttpResponse httpResponse, MIME mime) {
        DataOutputStream dos = new DataOutputStream(out);

        try {
            responseHeader(dos, httpResponse, mime);
            responseBody(dos, httpResponse);
            dos.flush();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private static void responseHeader(DataOutputStream dos, HttpResponse httpResponse, MIME mime) {
        try {
            dos.writeBytes(httpResponse.getStatusLine());
            dos.writeBytes("Content-Type: " + mime.getContentType() + ";charset=utf-8\r\n");
            dos.writeBytes("Content-Length: " + httpResponse.getBody().length + "\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private static void responseBody(DataOutputStream dos, HttpResponse httpResponse) {
        try {
            dos.write(httpResponse.getBody(), 0, httpResponse.getBody().length);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}
