package webserver.core.http.response;

import webserver.core.http.Header;

import java.io.DataOutputStream;
import java.io.IOException;

public class HttpResponse {
    private final HttpStatus status;
    private final Header header;
    private final byte[] body;

    public HttpResponse(HttpStatus status, Header header, byte[] body) {
        this.status = status;
        this.header = header;
        this.body = body;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public Header getHeader() {
        return header;
    }

    public byte[] getBody() {
        return body;
    }

    public void response(DataOutputStream dos) throws IOException {
        dos.writeBytes("HTTP/1.1 " + status.getStatusCode() + " " + status.getMessage() + "\r\n");
        for (String key : header) {
            dos.writeBytes(key + ": " + header.getHeaderValue(key) + "\r\n");
        }
        dos.writeBytes("Content-Length: " + body.length + "\r\n");
        dos.writeBytes("\r\n");
        dos.write(body, 0, body.length);
        dos.flush();
    }

}
