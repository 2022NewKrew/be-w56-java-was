package util.request;

public class RequestLine {
    private final String method;
    private final String url;
    private final String version;

    public RequestLine(String method, String url, String version) {
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
