package http;

public class HttpResponse {
    public static final String EMPTY_SPACE = " ";

    private StatusLine statusLine;
    private HttpHeader header;
    private byte[] body;

    public HttpResponse(String protocol, HttpStatus httpStatus, HttpHeader header) {
        this.statusLine = new StatusLine(protocol, httpStatus);
        this.header = header;
        this.body = new byte[1];
    }

    public HttpResponse(String protocol, HttpStatus httpStatus, HttpHeader header, byte[] body) {
        this.statusLine = new StatusLine(protocol, httpStatus);
        this.header = header;
        this.body = body;
    }

    public HttpHeader getHeader() {
        return header;
    }

    public String getStatusLineText() {
        return statusLine.getProtocol() + EMPTY_SPACE + statusLine.getStatusCode() + EMPTY_SPACE + statusLine.getStatusText();
    }

    public int getBodyLength() {
        return body.length;
    }

    public byte[] getBody() {
        return body;
    }
}
