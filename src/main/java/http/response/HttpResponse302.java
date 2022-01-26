package http.response;

import java.io.DataOutputStream;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.Constant;

public class HttpResponse302 implements HttpResponse {

    private static final Logger log = LoggerFactory.getLogger(HttpResponse302.class);

    private final DataOutputStream dos;
    private final String redirectUrl;

    public HttpResponse302(DataOutputStream dos, String redirectUrl) {
        this.dos = dos;
        this.redirectUrl = redirectUrl;
        log.info("302 FOUND");
    }

    @Override
    public void sendResponse() {
        responseHeader();
    }

    protected void responseHeader() {
        try {
            dos.writeBytes("HTTP/1.1 302 FOUND" + Constant.lineBreak);
            dos.writeBytes("Location: " + redirectUrl + Constant.lineBreak);
            dos.flush();
        } catch (IOException exception) {
            log.info(exception.getMessage());
        }
    }
}
