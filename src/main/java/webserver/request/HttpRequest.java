package webserver.request;

import webserver.response.HttpStatus;

public class HttpRequest {
    private HttpRequestLine httpRequestLine;
    private HttpRequestHeader httpRequestHeader;
    private HttpRequestBody httpRequestBody;

    public HttpRequest(HttpRequestLine httpRequestLine, HttpRequestHeader httpRequestHeader, HttpRequestBody httpRequestBody) {
        this.httpRequestLine = httpRequestLine;
        this.httpRequestHeader = httpRequestHeader;
        this.httpRequestBody = httpRequestBody;
    }

    public HttpRequestMethod getHttpRequestMethod() {
        return httpRequestLine.getMethod();
    }

    public HttpRequestLine getHttpRequestLine() {
        return httpRequestLine;
    }

    public HttpRequestUri getUri() {
        return httpRequestLine.getUri();
    }
}
