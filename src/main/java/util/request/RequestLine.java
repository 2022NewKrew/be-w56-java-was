package util.request;

import util.HttpMethod;

public class RequestLine {
    private final HttpMethod method;
    private final String url;
    private final String version;

    public RequestLine(String method, String url, String version) {
        this.method = HttpMethod.valueOf(method);
        this.url = url;
        this.version = version;
    }

    public HttpMethod getMethod() {
        return method;
    }

    public String getUrl() {
        return url;
    }

    public String getVersion() {
        return version;
    }
}
