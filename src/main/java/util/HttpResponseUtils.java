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

    public static void response(OutputStream out, MyHttpResponse myHttpResponse, String uri) {
        DataOutputStream dos = new DataOutputStream(out);

        try {
            responseHeader(dos, myHttpResponse, uri);
            responseBody(dos, myHttpResponse);
            dos.flush();
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private static void responseHeader(DataOutputStream dos, MyHttpResponse myHttpResponse, String uri) {
        try {
            dos.writeBytes(myHttpResponse.getStatusLine());
            if (myHttpResponse.getStatusLine().split(" ")[1].equals("302")) {
                dos.writeBytes("Location: " + uri + "\r\n");
            }
            dos.writeBytes("Content-Type: " + MIME.parse(uri).getContentType() + ";charset=utf-8\r\n");
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
