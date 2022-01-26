package web.http.request;

public class HttpRequestLine {
    private final String method;
    private final String url;
    private final String version;

    public HttpRequestLine(String method, String url, String version) {
        this.method = method;
        this.url = url;
        this.version = version;
    }

    public String getMethod() {
        return method;
    }

    public String getUrl() {
        return url;
    }

    public String getVersion() {
        return version;
    }
}
