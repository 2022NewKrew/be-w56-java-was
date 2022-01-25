package webserver.http.request;

import java.net.URI;

public class HttpRequest {

    private final HttpRequestLine requestLine;
    private final HttpRequestHeaders requestHeaders;
    private final HttpRequestBody requestBody;

    public HttpRequest(HttpRequestLine requestLine, HttpRequestHeaders requestHeaders, HttpRequestBody requestBody) {
        this.requestLine = requestLine;
        this.requestHeaders = requestHeaders;
        this.requestBody = requestBody;
    }

    public HttpRequestLine getRequestLine() {
        return requestLine;
    }

    public HttpRequestHeaders getRequestHeaders() {
        return requestHeaders;
    }

    public HttpRequestBody getRequestBody() {
        return requestBody;
    }

    public String getRequestPath() {
        return requestLine.getRequestPath();
    }

    public String getHttpVersion() {
        return requestLine.getHttpVersion();
    }

    public URI getUri() {
        return requestLine.getUri();
    }
}
