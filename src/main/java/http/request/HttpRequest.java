package http.request;

import http.HttpBody;
import http.HttpHeaders;
import http.HttpMethod;

public class HttpRequest {

    private final StartLine startLine;

    private final HttpHeaders headers;

    private final HttpBody body;

    private final RequestParams params;

    private HttpRequest(StartLine startLine, HttpHeaders headers, HttpBody body, RequestParams params) {
        this.startLine = startLine;
        this.headers = headers;
        this.body = body;
        this.params = params;
    }

    public static HttpRequest of(StartLine startLine, HttpHeaders headers, HttpBody body, RequestParams params) {
        return new HttpRequest(startLine, headers, body, params);
    }

    public String getUrl() {
        return startLine.getUrl().getPath();
    }

    public HttpMethod getMethod() {
        return startLine.getHttpMethod();
    }

    public String getHttpVersion() {
        return startLine.getHttpVersion();
    }

    public HttpBody getBody() {
        return body;
    }

    public Object getParameter(String key) {
        return params.getValue(key);
    }
}
