package util.response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.RequestHandler;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Map;

public class HttpResponseUtils {
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

    public static void httpResponse(DataOutputStream dos, Response response) {
        try {
            responseHeader(dos, response);
            responseBody(dos, response.getBody());
            dos.flush();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private static void responseHeader(DataOutputStream dos, Response response) throws IOException {
        dos.writeBytes("HTTP/1.1 " + response.getHttpStatus() + "\r\n");
        Map<String, String> header = response.getHeader();
        for (String key : header.keySet()) {
            String value = header.get(key);
            dos.writeBytes(key + ": " + value + "\r\n");
        }
        dos.writeBytes("\r\n");
    }

    public static void responseBody(DataOutputStream dos, byte[] body) throws IOException {
        if (body == null) {
            return;
        }
        dos.write(body, 0, body.length);
        dos.flush();
    }
}
