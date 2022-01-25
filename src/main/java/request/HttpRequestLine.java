package request;

public class HttpRequestLine {
    private final String method;
    private final String url;
    private final String httpVersion;

    public HttpRequestLine(String method, String url, String httpVersion) {
        this.method = method;
        this.url = url;
        this.httpVersion = httpVersion;
    }

    public String getUrl() {
        return url;
    }
}
