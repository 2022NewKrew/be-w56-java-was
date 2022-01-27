package http.response;

import java.io.DataOutputStream;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.Constant;

public class HttpResponse400 implements HttpResponse {

    private static final Logger log = LoggerFactory.getLogger(http.response.HttpResponse404.class);

    private final DataOutputStream dos;
    private final byte[] body;

    public HttpResponse400(DataOutputStream dos, byte[] body) {
        this.dos = dos;
        this.body = body;
    }

    @Override
    public void sendResponse() {
        responseHeader();
        responseBody();
        log.info("400 BAD REQUEST");
    }

    private void responseHeader() {
        try {
            dos.writeBytes("HTTP/1.1 400 BAD REQUEST" + Constant.lineBreak);
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
