package util;

import model.MyHttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.enums.MIME;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class HttpResponseUtils {
    private static final Logger logger = LoggerFactory.getLogger(HttpResponseUtils.class);

    public static void response(OutputStream out, MyHttpResponse myHttpResponse, MIME mime) {
        DataOutputStream dos = new DataOutputStream(out);

        try {
            responseHeader(dos, myHttpResponse, mime);
            responseBody(dos, myHttpResponse);
            dos.flush();
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private static void responseHeader(DataOutputStream dos, MyHttpResponse myHttpResponse, MIME mime) {
        try {
            dos.writeBytes(myHttpResponse.getStatusLine());
            dos.writeBytes("Content-Type: " + mime.getContentType() + ";charset=utf-8\r\n");
            dos.writeBytes("Content-Length: " + myHttpResponse.getBody().length + "\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private static void responseBody(DataOutputStream dos, MyHttpResponse myhttpResponse) {
        try {
            dos.write(myhttpResponse.getBody(), 0, myhttpResponse.getBody().length);
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }
}
