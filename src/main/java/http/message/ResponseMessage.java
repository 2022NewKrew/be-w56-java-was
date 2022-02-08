package http.message;

import http.body.ResponseBody;
import http.header.Cookie;
import http.header.Headers;
import http.header.MimeType;
import http.startline.HttpStatus;
import http.startline.StatusLine;

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

    public static ResponseMessage create(HttpStatus status) {
        ResponseBody responseBody = new ResponseBody();
        StatusLine statusLine = StatusLine.create(status);
        Headers responseHeaders = responseBody.createResponseHeader();
        return new ResponseMessage(statusLine, responseHeaders, responseBody);
    }

    public static ResponseMessage create(HttpStatus status, MimeType mimeType, byte[] file, Cookie cookie) {
        ResponseBody responseBody = new ResponseBody(file);
        StatusLine statusLine = StatusLine.create(status);
        Headers responseHeaders = responseBody.createResponseHeader(mimeType, cookie);
        return new ResponseMessage(statusLine, responseHeaders, responseBody);
    }

    public static ResponseMessage create(HttpStatus status, String url, Cookie cookie) {
        ResponseBody responseBody = new ResponseBody();
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
