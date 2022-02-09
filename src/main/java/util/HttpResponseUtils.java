package util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.RequestHandler;

import java.io.DataOutputStream;
import java.io.IOException;

public class HttpResponseUtils {
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

    public static void responseHeader(DataOutputStream dos, HttpResponse response) {
        try {
            switch (response.getStatusCode()) {
                case "200":
                    dos.writeBytes(response.getHttpVersion() + " 200 OK \r\n");
                    dos.writeBytes(response.getTotalHeader());
//                    dos.writeBytes("Content-Type: " + response.getHeaderElem("Content-Type") + ";charset=utf-8\r\n");
//                    dos.writeBytes("Content-Length: " + response.getHeaderElem("Content-Length") + "\r\n");
                    dos.writeBytes("\r\n");
                    break;
                case "302":
                    dos.writeBytes(response.getHttpVersion() + " 302 Found \r\n");
                    dos.writeBytes(response.getTotalHeader());
//                    dos.writeBytes("Location: " + response.getHeaderElem("Location") + "\r\n");
//                    dos.writeBytes(re);
                    dos.writeBytes("\r\n");
                    break;
                default:
                    break;
            }
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    public static void responseBody(DataOutputStream dos, byte[] body) {
        try {
            dos.write(body, 0, body.length);
            dos.flush();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}
