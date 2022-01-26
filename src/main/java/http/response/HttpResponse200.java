package http.response;

import java.io.DataOutputStream;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.Constant;

public class HttpResponse200 implements HttpResponse {

    private static final Logger log = LoggerFactory.getLogger(HttpResponse200.class);

    private final DataOutputStream dos;
    private final byte[] body;
    private final String contentType;

    public HttpResponse200(DataOutputStream dos, String contentType, byte[] body) {
        this.dos = dos;
        this.body = body;
        this.contentType = contentType;
    }

    @Override
    public void sendResponse() {
        responseHeader();
        responseBody();
        log.info("200 OK");
    }

    private void responseHeader() {
        try {
            dos.writeBytes("HTTP/1.1 200 OK" + Constant.lineBreak);
            dos.writeBytes("Content-Type: " + contentType + Constant.lineBreak);
            dos.writeBytes(Constant.lineBreak);
        } catch (IOException exception) {
            log.info(exception.getMessage());
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
