package util;

import lombok.extern.slf4j.Slf4j;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

@Slf4j
public class HttpResponseUtils {

    public static void writeStatusCode(DataOutputStream dos, int statusCode) {
        try {
            String str = String.format("HTTP/1.1 %d\r\n", statusCode);
            dos.writeBytes(str);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    public static void writeContentType(DataOutputStream dos, String contentType) {
        try {
            String str = String.format("Content-Type: %s ;charset=utf-8\r\n", contentType);
            dos.writeBytes(str);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    public static void writeBody(DataOutputStream dos, byte[] body) {
        int contentLength = body.length;
        String str = String.format("Content-Length: %d\r\n\r\n", contentLength);
        try {
            dos.writeBytes(str);
            dos.write(body, 0, contentLength);
            dos.flush();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    public static void writeLocation(DataOutputStream dos, String location) {
        String str = String.format("Location: %s\r\n", location);
        try {
            dos.writeBytes(str);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    public static void writeCookie(DataOutputStream dos, String key, String value, String path) {
        String str = String.format("Set-Cookie: %s=%s; path=%s\r\n", key, value, path);
        try {
            dos.writeBytes(str);
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

    public static void response404Header(DataOutputStream dos, int lengthOfBodyContent) {
        try {
            dos.writeBytes("HTTP/1.1 404 \r\n");
            dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    public static void response404(DataOutputStream dos) {
        byte[] outputBody;
        File file = new File("./webapp/404page.html");
        try {
            outputBody = Files.readAllBytes(file.toPath());
            HttpResponseUtils.writeStatusCode(dos, 404);
            HttpResponseUtils.writeBody(dos, outputBody);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}
