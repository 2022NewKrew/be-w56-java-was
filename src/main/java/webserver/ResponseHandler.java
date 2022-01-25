package webserver;

import domain.Constants;
import domain.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.DataOutputStream;
import java.io.IOException;

public class ResponseHandler {

    private static final Logger log = LoggerFactory.getLogger(ResponseHandler.class);

    private final DataOutputStream dos;

    public ResponseHandler(DataOutputStream dos) {
        this.dos = dos;
    }

    public void responseHeader(HttpStatus status, int lengthOfBodyContent, String contentType) {
        try {
            dos.writeBytes("HTTP/1.1 " + status + Constants.LINE_DELIMITER);
            dos.writeBytes("Content-Type: " + contentType + ";charset=utf-8" + Constants.LINE_DELIMITER);
            dos.writeBytes("Content-Length: " + lengthOfBodyContent + Constants.LINE_DELIMITER);
            dos.writeBytes(Constants.LINE_DELIMITER);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }

    public void responseBody(byte[] body) {
        try {
            dos.write(body, 0, body.length);
            dos.flush();
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }
}
