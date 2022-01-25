package request;

public class HttpRequest {
    private final HttpRequestLine httpRequestLine;
    private final HttpRequestHeader httpRequestHeader;

    public HttpRequest(HttpRequestLine httpRequestLine, HttpRequestHeader httpRequestHeader) {
        this.httpRequestLine = httpRequestLine;
        this.httpRequestHeader = httpRequestHeader;
    }
}
