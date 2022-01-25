package http.response;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpResponse {

    private static final Logger log = LoggerFactory.getLogger(HttpResponse.class);

    private final DataOutputStream dos;
    private final StatusCode statusCode;
    private final String contentType;
    private final String requiredUrl;
    private final byte[] body;

    public HttpResponse(DataOutputStream dos, StatusCode statusCode, String requiredUrl,
            byte[] body) {
        log.info(String.valueOf(body.length));
        this.dos = dos;
        this.statusCode = statusCode;
        this.requiredUrl = requiredUrl;
        this.body = body;

        List<String> splitUrl = List.of(requiredUrl.split("\\."));
        String extension = splitUrl.get(splitUrl.size() - 1);
        contentType = ContentType.findTypeByExtension(extension).getType();
    }

    public void sendResponse() {
        responseHeader();
        responseBody();
    }

    private void responseHeader() {
        try {
            dos.writeBytes("HTTP/1.1 " + statusCode.getStatus() + " \r\n");
            dos.writeBytes("Content-Type: " + contentType + "\r\n");
            dos.writeBytes("\r\n");
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
