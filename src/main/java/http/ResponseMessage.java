package http;

public class ResponseMessage {
    private final StatusLine statusLine;
    private final Headers headers;
    private final Body body;

    public ResponseMessage(StatusLine statusLine, Headers headers, Body body) {
        validateNull(statusLine, headers);
        this.statusLine = statusLine;
        this.headers = headers;
        this.body = body;
    }

    void validateNull(StatusLine statusLine, Headers headers) {
        if(statusLine == null || headers == null) {
            throw new IllegalArgumentException();
        }
    }

    public StatusLine getStatusLine() {
        return statusLine;
    }

    public Headers getHeaders() {
        return headers;
    }

    public Body getBody() {
        return body;
    }
}
