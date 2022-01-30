package framework.http.response;

public class HttpResponse {
    public static final String DEFAULT_PROTOCOL = "HTTP/1.1";

    private StatusLine statusLine;
    private HttpResponseHeader header;
    private byte[] body;

    public HttpResponse(HttpStatus httpStatus, HttpResponseHeader header) {
        this.statusLine = new StatusLine(DEFAULT_PROTOCOL, httpStatus);
        this.header = header;
        this.body = new byte[1];
    }

    public HttpResponse(HttpStatus httpStatus, HttpResponseHeader header, byte[] body) {
        this.statusLine = new StatusLine(DEFAULT_PROTOCOL, httpStatus);
        this.header = header;
        this.body = body;
    }

    public HttpResponse(String protocol, HttpStatus httpStatus, HttpResponseHeader header) {
        this.statusLine = new StatusLine(protocol, httpStatus);
        this.header = header;
        this.body = new byte[1];
    }

    public HttpResponse(String protocol, HttpStatus httpStatus, HttpResponseHeader header, byte[] body) {
        this.statusLine = new StatusLine(protocol, httpStatus);
        this.header = header;
        this.body = body;
    }

    public HttpResponseHeader getHeader() {
        return header;
    }

    public String getStatusLineText() {
        return statusLine.getStatusLineText();
    }

    public int getBodyLength() {
        return body.length;
    }

    public byte[] getBody() {
        return body;
    }
}
