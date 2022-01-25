package webserver.http;

public class HttpResponse {

    private final HttpVersion version;
    private final HttpHeaders trailingHeaders;
    private final HttpResponseStatus status;
    private final byte[] body;


    public HttpResponse(HttpResponseStatus status) {
        this(new HttpVersion("HTTP/1.1"), status, new byte[0]);
    }

    public HttpResponse(HttpResponseStatus status, byte[] body) {
        this(new HttpVersion("HTTP/1.1"), status, body);
    }

    public HttpResponse(HttpVersion version, HttpResponseStatus status, byte[] body) {
        this.version = version;
        this.status = status;
        this.body = body;
        this.trailingHeaders = new HttpHeaders();
    }

    public HttpHeaders headers() {
        return trailingHeaders;
    }

    public HttpVersion getVersion() {
        return version;
    }

    public HttpResponseStatus getStatus() {
        return status;
    }

    public byte[] getBody() {
        return body;
    }
}
