package model;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpClientErrorResponse extends HttpResponse{

    private final byte[] body;
    private static final Logger log = LoggerFactory.getLogger(HttpClientErrorResponse.class);

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
