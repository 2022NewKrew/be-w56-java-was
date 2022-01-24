package model;

public class RequestLine {
    private final String method;
    private final String url;
    private final String version;

    private RequestLine(String method, String url, String version) {
        this.method = method;
        this.url = url;
        this.version = version;
    }

    public static RequestLine from(String[] requestLineTokens) {
        return new RequestLine(requestLineTokens[0], requestLineTokens[1], requestLineTokens[2]);
    }

    public String getUrl() {
        return url;
    }
}
