package http.request;

public class HttpRequestStartLine {
    private final String method;
    private final String targetUri;
    private final String httpVersion;

    public HttpRequestStartLine(String method, String targetUri, String httpVersion) {
        this.method = method;
        this.targetUri = targetUri;
        this.httpVersion = httpVersion;
    }

    public String getMethod() {
        return method;
    }

    public String getTargetUri() {
        return targetUri;
    }

    public String getHttpVersion() {
        return httpVersion;
    }
}
