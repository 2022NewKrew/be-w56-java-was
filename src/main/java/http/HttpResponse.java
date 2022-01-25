package http;

import java.io.DataOutputStream;
import java.io.IOException;

public class HttpResponse {

    private final HttpVersion version;
    private final HttpStatus status;
    private final HttpHeaders headers;
    private final String view;
    private byte[] body;

    public HttpResponse(HttpVersion version, HttpStatus status, HttpHeaders headers,
        String view, byte[] body) {
        this.version = version;
        this.status = status;
        this.headers = headers;
        this.view = view;
        this.body = body;
    }

    public String statusLine() {
        return version.toString() + status.toString();
    }

    public String header() {
        return headers.toString();
    }

    public void writeBody(byte[] body) {
        this.body = body;
        this.headers.put(HttpHeaders.CONTENT_LENGTH, Integer.toString(body.length));
    }

    public String getView() {
        return view;
    }

    public void write(DataOutputStream dos) throws IOException {
        dos.writeBytes(statusLine());
        dos.writeBytes(header());
        dos.writeBytes("\r\n");
        dos.write(body);
    }
}
