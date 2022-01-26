package http.response;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.Constant;

public class HttpResponse {

    private static final Logger log = LoggerFactory.getLogger(HttpResponse.class);

    private final DataOutputStream dos;
    private final StatusCode statusCode;
    private final String contentType;
    private final byte[] body;

    public HttpResponse(DataOutputStream dos, StatusCode statusCode, String contentType,
            byte[] body) {
        log.info(String.valueOf(body.length));
        this.dos = dos;
        this.statusCode = statusCode;
        this.contentType = contentType;
        this.body = body;
    }

    public void sendResponse() {
        responseHeader();
        responseBody();
    }

    private void responseHeader() {
        try {
            dos.writeBytes("HTTP/1.1 " + statusCode.getStatus() + " " + Constant.lineBreak);
            dos.writeBytes("Content-Type: " + contentType + Constant.lineBreak);
            dos.writeBytes(Constant.lineBreak);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void responseBody() {
        try {
            dos.write(body);
            dos.flush();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}
