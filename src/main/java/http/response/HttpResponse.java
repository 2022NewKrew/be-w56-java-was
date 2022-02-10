package http.response;

import java.io.DataOutputStream;
import java.io.IOException;

public class HttpResponse {
    private final HttpResponseHeader header;
    private final HttpResponseBody body;

    public HttpResponse(HttpResponseHeader header, HttpResponseBody body) {
        this.header = header;
        this.body = body;
    }

    public void writeToDataOutputStream(DataOutputStream dos) {
        try {
            header.writeToDataOutputStream(dos);
            body.writeToDataOutputStream(dos);
        } catch (IOException e) {
            throw new RuntimeException("Failed to write HttpResponse to dos");
        }
    }

    public HttpResponseHeader header() { return header; }
}
