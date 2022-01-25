package http.request;

public class HttpRequest {
    private final HttpRequestStartLine httpRequestStartLine;
    private final HttpRequestHeader httpRequestHeader;
    private final HttpRequestBody httpRequestBody;

    public HttpRequest(HttpRequestStartLine httpRequestStartLine, HttpRequestHeader httpRequestHeader, HttpRequestBody httpRequestBody) {
        this.httpRequestStartLine = httpRequestStartLine;
        this.httpRequestHeader = httpRequestHeader;
        this.httpRequestBody = httpRequestBody;
    }

    public HttpRequestStartLine getHttpRequestStartLine() {
        return httpRequestStartLine;
    }

    public HttpRequestHeader getHttpRequestHeader() {
        return httpRequestHeader;
    }

    public HttpRequestBody getHttpRequestBody() {
        return httpRequestBody;
    }
}
