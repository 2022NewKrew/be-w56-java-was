package webserver;

import com.google.common.net.MediaType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.DataOutputStream;
import java.io.IOException;

public class ResponseHandler {
    private static final Logger log = LoggerFactory.getLogger(ResponseHandler.class);

    public static MediaType getMediaType(String url) {
        if (url.contains(".css")) return MediaType.CSS_UTF_8;
        if (url.contains(".html")) return MediaType.HTML_UTF_8;
        if (url.contains(".js")) return MediaType.JAVASCRIPT_UTF_8;
        return MediaType.ANY_TYPE;
    }

    public static void response200HeaderWithCookie(DataOutputStream dos, int lengthOfBodyContent, String cookie, MediaType mediaType) {
        try {
            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            dos.writeBytes(String.format("Content-Type: %s\r\n", mediaType.toString()));
            dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
            dos.writeBytes("Vary: " + "Accept-Encoding" + "\r\n");
            if (!cookie.isBlank()) dos.writeBytes(String.format("Set-Cookie: %s" + "\r\n", cookie));
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    public static void response200Header(DataOutputStream dos, int lengthOfBodyContent, MediaType mediaType) {
        try {
            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            dos.writeBytes(String.format("Content-Type: %s\r\n", mediaType.toString()));
            dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
            dos.writeBytes("Vary: " + "Accept-Encoding" + "\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    public static void redirectHeader(DataOutputStream dos, String redirectUrl) {
        try {
            dos.writeBytes("HTTP/1.1 302 Found \r\n");
            dos.writeBytes(String.format("Location: %s" + "\r\n", redirectUrl));
            dos.writeBytes("\r\n");
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
