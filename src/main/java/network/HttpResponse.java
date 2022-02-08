package network;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;

public class HttpResponse {
    private static final Logger log = LoggerFactory.getLogger(HttpResponse.class);

    public static void handleHtmlResponse(String path, OutputStream out, Status status) throws IOException {

        DataOutputStream dos = new DataOutputStream(out);

        switch (status) {
            case OK:
                byte[] body = getHtmlBytes(path);
                response200Header(dos, body.length);
                responseBody(dos, body);
                break;
            case FOUND:
                response302Header(dos, path);
                break;
        }
    }

    public static void redirect(String path, OutputStream out){
        DataOutputStream dos = new DataOutputStream(out);
        response302Header(dos, path);
    }

    private static byte[] getHtmlBytes(String url) throws IOException {
        return Files.readAllBytes(new File("./webapp" + url).toPath());
    }

    private static void response200Header(DataOutputStream dos, int lengthOfBodyContent) {
        try {
            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            dos.writeBytes("Content-Type: text/html;charset=utf-8\r\n");
            dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private static void response302Header(DataOutputStream dos, String url) {
        try {
            dos.writeBytes("HTTP/1.1 302 Found \r\n");
            dos.writeBytes("Location: " + url);
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private static void responseBody(DataOutputStream dos, byte[] body) {
        try {
            dos.write(body, 0, body.length);
            dos.flush();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}
