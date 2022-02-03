package http;

public class ResponseMessage {
    private final StatusLine statusLine;
    private final Headers headers;
    private final ResponseBody responseBody;

    private ResponseMessage(StatusLine statusLine, Headers headers, ResponseBody responseBody) {
        validateNull(statusLine, headers);
        this.statusLine = statusLine;
        this.headers = headers;
        this.responseBody = responseBody;
    }

    public static ResponseMessage create(HttpStatus status, byte[] file) {
        ResponseBody responseBody = new ResponseBody(file);
        StatusLine statusLine = StatusLine.create(status);
        Headers responseHeaders = responseBody.createResponseHeader();
        return new ResponseMessage(statusLine, responseHeaders, responseBody);
    }

    public static ResponseMessage create(HttpStatus status, String url, Cookie cookie) {
        ResponseBody responseBody = new ResponseBody(new byte[]{});
        StatusLine statusLine = StatusLine.create(status);
        Headers responseHeaders = responseBody.createResponseHeader(url, cookie);
        return new ResponseMessage(statusLine, responseHeaders, responseBody);
    }

    void validateNull(StatusLine statusLine, Headers headers) {
        if (statusLine == null || headers == null) {
            throw new IllegalArgumentException();
        }
    }

    public StatusLine getStatusLine() {
        return statusLine;
    }

    public Headers getHeaders() {
        return headers;
    }

    public ResponseBody getBody() {
        return responseBody;
    }
}
