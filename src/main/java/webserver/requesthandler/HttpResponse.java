package webserver.requesthandler;

import lombok.extern.slf4j.Slf4j;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

@Slf4j
public class HttpResponse {
    public static void doResponse(OutputStream out, Map<String, String> headers, byte[] body) {
        log.debug("[HTTP Response]");
        DataOutputStream dos = new DataOutputStream(out);
        outputHeader(dos, body.length, headers.get("Accept").split(",")[0]);
        outputBody(dos, body);
    }

    private static void outputHeader(DataOutputStream dos, int lengthOfBodyContent, String contentType) {
        try {
            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            dos.writeBytes("Content-Type: " + contentType + ";charset=utf-8\r\n");
            dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private static void outputBody(DataOutputStream dos, byte[] body) {
        try {
            dos.write(body, 0, body.length);
            dos.flush();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}
