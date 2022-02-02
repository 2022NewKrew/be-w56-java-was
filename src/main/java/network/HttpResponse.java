package network;

import java.util.List;

public class HttpResponse {

    private final List<String> headers;
    private byte[] body = new byte[0];

    public HttpResponse(List<String> headers) {
        this.headers = headers;
    }

    public HttpResponse(List<String> headers, byte[] body) {
        this.headers = headers;
        this.body = body;
    }

    public List<String> getHeaders() {
        return headers;
    }

    public byte[] getBody() {
        return body;
    }
}
