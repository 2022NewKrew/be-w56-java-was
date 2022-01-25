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

    public void writeToDataOutputStream(DataOutputStream dos) throws IOException {
        try {
            header.writeToDataOutputStream(dos);
            body.writeToDataOutputStream(dos);
        } catch (IOException e) {
            throw new IOException("Failed to write HttpResponse to dos");
        }
    }
}
