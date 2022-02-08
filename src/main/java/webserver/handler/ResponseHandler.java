package webserver.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.http.*;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.List;

public class ResponseHandler {

    private static final Logger log = LoggerFactory.getLogger(ResponseHandler.class);

    private static final String CRLF = "\r\n";
    private static final String HEADER_VALUE_DELIMITER = ",";

    public static void sendResponse(HttpResponse response, DataOutputStream dos) {
        if (response != null) {
            writeBytes(response, dos);
            flush(dos);
        }
    }

    private static void writeBytes(HttpResponse response, DataOutputStream dos) {
        String version = response.getVersion();
        HttpStatus status = response.getStatus();
        HttpHeader headers = response.getHeaders();
        HttpCookie cookies = response.getCookies();
        String contentType = String.join(";", response.getContentType());
        int contentLength = response.getContentLength();
        byte[] body = response.getBody();

        try {
            dos.writeBytes(String.format("%s %s %s", version, status, CRLF));
            writeBytesHeaders(headers, dos);
            dos.writeBytes(String.format("Content-Type: %s%s", contentType, CRLF));
            writeBytesCookies(cookies, dos);
            dos.writeBytes(String.format("Content-Length: %s%s", contentLength, CRLF));
            dos.writeBytes(CRLF);
            dos.write(body, 0, contentLength);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private static void writeBytesHeaders(HttpHeader headers, DataOutputStream dos) throws IOException {
        for (String key : headers.keySet()) {
            List<String> values = headers.getValues(key);
            String joinedValues = String.join(HEADER_VALUE_DELIMITER, values);
            dos.writeBytes(String.format("%s: %s%s", key, joinedValues, CRLF));
        }
    }

    private static void writeBytesCookies(HttpCookie cookies, DataOutputStream dos) throws IOException {
        for (Cookie cookie : cookies.iterator()) {
            dos.writeBytes(String.format("Set-Cookie: %s%s", cookie, CRLF));
        }
    }

    private static void flush(DataOutputStream dos) {
        try {
            dos.flush();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}
