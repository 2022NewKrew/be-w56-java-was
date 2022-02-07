package webserver.http;

public class HttpRequest {

    private final HttpVersion version;
    private final HttpMethod method;
    private final String uri;
    private final HttpHeader trailingHeaders;
    private final HttpRequestParams params;
    private final String body;


    public HttpRequest(HttpVersion version, HttpMethod method, String uri, HttpRequestParams params,
        HttpHeader headers, String body) {
        this.version = version;
        this.method = method;
        this.uri = uri;
        this.params = params;
        this.trailingHeaders = headers;
        this.body = body;
    }

    public HttpVersion getVersion() {
        return version;
    }

    public HttpMethod getMethod() {
        return method;
    }

    public String getUri() {
        return uri;
    }

    public HttpHeader getTrailingHeaders() {
        return trailingHeaders;
    }

    public HttpRequestParams getParams() {
        return params;
    }

    public String getBody() {
        return body;
    }
}
