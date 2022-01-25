package webserver.manage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.RequestHandler;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class ResponseFormat {
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

    private DataOutputStream dos;
    private ResponseFile responseFile;

    public ResponseFormat(OutputStream os, String filePath) {
        this.dos = new DataOutputStream(os);
        this.responseFile = new ResponseFile(filePath);
    }

    public void sendResponse (ResponseCode status) throws IOException {
        byte[] body = responseFile.getFileBytes();
        switch (status) {
            case STATUS_200:
                response200Header(body.length);
                break;
            case STATUS_302:
                response302Header(body.length);
                break;
        }
        responseBody(body);
    }

    private void response200Header(int lengthOfBodyContent) {
        try {
            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            dos.writeBytes("Content-Type: "+responseFile.getFileType()+";charset=utf-8\r\n");
            dos.writeBytes("Content-Length: "+lengthOfBodyContent+"\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void response302Header(int lengthOfBodyContent) {
        try {
            dos.writeBytes("HTTP/1.1 303 See Other\r\n");
            dos.writeBytes("Location: /index.html \r\n");
//            dos.writeBytes("Content-Type: "+responseFile.getFileType()+";charset=utf-8\r\n");
//            dos.writeBytes("Content-Length: "+lengthOfBodyContent+"\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void responseBody(byte[] body) {
        try {
            dos.write(body, 0, body.length);
            dos.flush();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}
