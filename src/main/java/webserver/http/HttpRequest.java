package webserver.http;

import webserver.WebServerConfig;

public class HttpRequest {

    private final HttpVersion version;
    private final HttpMethod method;
    private final String uri;
    private final HttpHeaders trailingHeaders;
    private final HttpRequestParams params;


    public HttpRequest(HttpVersion version, HttpMethod method, String uri, HttpRequestParams params,
        HttpHeaders headers) {
        this.version = version;
        this.method = method;
        this.uri = uri.equals(WebServerConfig.ROOT_PATH) ? WebServerConfig.ENTRY_FILE : uri;
        this.params = params;
        this.trailingHeaders = headers;
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

    public HttpHeaders getTrailingHeaders() {
        return trailingHeaders;
    }

    public HttpRequestParams getParams() {
        return params;
    }
}
