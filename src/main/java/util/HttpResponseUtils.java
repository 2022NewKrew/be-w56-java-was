package util;

import lombok.extern.slf4j.Slf4j;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

@Slf4j
public class HttpResponseUtils {
    public static void make200Response(DataOutputStream dos, String ret) {
        try {
            byte[] body = Files.readAllBytes(new File("./webapp" + ret).toPath());
            response200Header(dos, getContentType(ret), body.length);
            responseBody(dos, body);
        } catch (IOException e) {
            log.error("Cannot Found Resource : {}", e.getMessage());
            response404NotFound(dos);
        }
    }

    public static void response404NotFound(DataOutputStream dos) {
        try {
            dos.writeBytes("HTTP/1.1 404 Not Found \r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    public static void response200Header(DataOutputStream dos, String contentType, int lengthOfBodyContent) {
        try {
            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            dos.writeBytes("Content-Type: " + contentType + ";charset=utf-8\r\n");
            dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
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

    //TODO enum 객체로 치환
    private static String getContentType(String uri) {
        if (uri.endsWith(".js")) {
            return "text/javascript";
        }

        if (uri.endsWith(".css")) {
            return "text/css";
        }

        return "text/html";
    }
}
