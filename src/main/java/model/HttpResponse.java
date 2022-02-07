package model;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpResponse {

    private static final Logger log = LoggerFactory.getLogger(HttpResponse.class);

    public static HttpResponse of(HttpStatus httpStatus, byte[] body, String mime) {

        StatusLine statusLine = new StatusLine(HttpVersion.HTTP_1_1.getVersion(), httpStatus.getCode(),
                httpStatus.getMessage());
        Map<String, String> headerKeyMap = Map.of(
                "Content-Type", mime,
                "Content-Length", Integer.toString(body.length)
        );
        Header header = new Header(headerKeyMap);

        return new HttpResponse(statusLine, header, body);
    }

    private final StatusLine statusLine;
    private final Header header;
    private final byte[] body;

    private HttpResponse(StatusLine statusLine, Header header, byte[] body) {
        log.debug("HttpResponse: \n{}\n {}\n", statusLine, header);
        this.statusLine = statusLine;
        this.header = header;
        this.body = body;
    }

    public void sendResponse(Socket connection) {
        try (OutputStream out = connection.getOutputStream();) {
            DataOutputStream dos = new DataOutputStream(out);

            responseHeader(dos);
            responseBody(dos);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void responseHeader(DataOutputStream dos) {
        try {
            dos.writeBytes(statusLine.message() + "\r\n");
            header.messageList().forEach(str -> {
                try {
                    dos.writeBytes(str + "\r\n");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void responseBody(DataOutputStream dos) {
        try {
            dos.write(body, 0, body.length);
            dos.flush();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}
