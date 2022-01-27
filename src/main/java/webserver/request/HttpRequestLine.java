package webserver.request;

public class HttpRequestLine {
    private HttpRequestMethod method;
    private HttpRequestUri uri;
    private String httpVersion;

    public HttpRequestLine(HttpRequestMethod method, HttpRequestUri uri, String httpVersion) {
        this.method = method;
        this.uri = uri;
        this.httpVersion = httpVersion;
    }

    public HttpRequestMethod getMethod() {
        return method;
    }

    public HttpRequestUri getUri() {
        return uri;
    }
}
