package webserver.response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpRequestUtils;
import webserver.header.ContentType;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class ResponseHandler {

    private static final Logger log = LoggerFactory.getLogger(ResponseHandler.class);

    private final DataOutputStream dos;

    private static final String NEXT_LINE = "\r\n";

    public ResponseHandler(DataOutputStream dos) {
        this.dos = dos;
    }

    public void response(String result) throws IOException {
        String[] parsed = result.split(":");
        if(parsed.length == 2) {
            response302Header(parsed[1]);
        }
        else {
            ContentType contentType = HttpRequestUtils.parseExtension(result);
            byte[] body = Files.readAllBytes(new File("./webapp" + result).toPath());
            response200Header(body.length, contentType);
            responseBody(body);
        }
    }

    private void response302Header(String location) {
        try {
            dos.writeBytes("HTTP/1.1 302 FOUND "+NEXT_LINE);
            dos.writeBytes("Location: " + location + NEXT_LINE);
            dos.writeBytes(NEXT_LINE);
            dos.flush();
        } catch (IOException e) {
            e.printStackTrace();
            log.error(e.getMessage());
        }
    }

    private void response200Header(int lengthOfBodyContent, ContentType contentType) {
        try {
            dos.writeBytes("HTTP/1.1 200 OK "+NEXT_LINE);
            dos.writeBytes("Content-Type: "+contentType.getType()+";charset=utf-8"+NEXT_LINE);
            dos.writeBytes("Content-Length: " + lengthOfBodyContent + NEXT_LINE);
            dos.writeBytes(NEXT_LINE);
        } catch (IOException e) {
            e.printStackTrace();
            log.error(e.getMessage());
        }
    }

    private void responseBody(byte[] body) {
        try {
            dos.write(body, 0, body.length);
            dos.flush();
        } catch (IOException e) {
            e.printStackTrace();
            log.error(e.getMessage());
        }
    }
}
