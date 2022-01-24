package webserver.http;

import webserver.WebServerConfig;

public class HttpRequest {

    private final HttpMethod method;
    private final String uri;

    public HttpRequest(HttpMethod method, String uri) {
        this.method = method;
        this.uri = uri.equals(WebServerConfig.ROOT_PATH) ? WebServerConfig.ENTRY_FILE : uri;
    }

    public HttpMethod getMethod() {
        return method;
    }

    public String getUri() {
        return uri;
    }
}
