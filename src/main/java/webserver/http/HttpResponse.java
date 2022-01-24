package webserver.http;

import java.nio.charset.StandardCharsets;

public class HttpResponse {

    private final HttpResponseHeader header;
    private final HttpResponseStatus status;
    private final byte[] body;

    public HttpResponse() {
        this(HttpResponseStatus.OK, new byte[0]);
    }

    public HttpResponse(HttpResponseStatus status, byte[] body) {
        this.status = status;
        this.body = body;
        this.header = new HttpResponseHeader(status, body.length);
    }

    public HttpResponseHeader getHeader() {
        return header;
    }

    public HttpResponseStatus getStatus() {
        return status;
    }

    public String getBodyString() {
        return new String(body, StandardCharsets.UTF_8);
    }
}
