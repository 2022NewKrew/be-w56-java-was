package model;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpResponse {

    private final Logger log = LoggerFactory.getLogger(HttpResponse.class);
    private final StatusLine statusLine;
    private final Header header;

    public HttpResponse(StatusLine statusLine, Header header) {
        this.statusLine = statusLine;
        this.header = header;
    }

    public void sendResponse(Socket connection) {
        try (OutputStream out = connection.getOutputStream();) {
            DataOutputStream dos = new DataOutputStream(out);

            responseHeader(dos);
            dos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected void responseHeader(DataOutputStream dos) {
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
}
