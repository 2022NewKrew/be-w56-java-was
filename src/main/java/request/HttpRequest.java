package request;

public class HttpRequest {
    private final HttpRequestLine httpRequestLine;
    private final HttpRequestHeader httpRequestHeader;

    public HttpRequest(HttpRequestLine httpRequestLine, HttpRequestHeader httpRequestHeader) {
        this.httpRequestLine = httpRequestLine;
        this.httpRequestHeader = httpRequestHeader;
    }

    public HttpRequestLine getHttpRequestLine() {
        return httpRequestLine;
    }

    public String getUrl() {
        return httpRequestLine.getUrl();
    }

    public String getMethod() {
        return httpRequestLine.getMethod();
    }
}
