package webserver.http.request;

public class HttpRequestLine {

    private final Method method;
    private final String uri;
    private final String httpVersion;

    public HttpRequestLine(Method method, String uri, String httpVersion) {
        this.method = method;
        this.uri = uri;
        this.httpVersion = httpVersion;
    }

    public Method getMethod() {
        return method;
    }

    public String getUri() {
        return uri;
    }

    public String getHttpVersion() {
        return httpVersion;
    }
}
