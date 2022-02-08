package model;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import view.View;

public class HttpClientErrorResponse extends HttpResponse {

    private static final Logger log = LoggerFactory.getLogger(HttpClientErrorResponse.class);

    public static HttpClientErrorResponse of(HttpStatus httpStatus, String url) throws IOException {
        StatusLine statusLine = new StatusLine(HttpVersion.HTTP_1_1.getVersion(), httpStatus.getCode(),
                httpStatus.getMessage());
        byte[] body = View.get(url);
        Map<String, String> headerKeyMap = Map.of(
                "Content-Type", Mime.HTML.getType(),
                "Content-Length", Integer.toString(body.length)
        );
        return new HttpClientErrorResponse(statusLine, new Header(headerKeyMap), body);
    }

    private final byte[] body;

    public HttpClientErrorResponse(StatusLine statusLine, Header header, byte[] body) {
        super(statusLine, header);
        this.body = body;
    }

    private void responseBody(DataOutputStream dos) {
        try {
            dos.write(body, 0, body.length);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    @Override
    public void sendResponse(Socket connection) {
        try (OutputStream out = connection.getOutputStream();) {
            DataOutputStream dos = new DataOutputStream(out);

            responseHeader(dos);
            responseBody(dos);
            dos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
