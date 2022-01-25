package webserver.http;

import webserver.WebServerConfig;

public class HttpRequest {

    private final HttpVersion version;
    private final HttpMethod method;
    private final String uri;

    public HttpRequest(HttpVersion version, HttpMethod method, String uri) {
        this.version = version;
        this.method = method;
        this.uri = uri.equals(WebServerConfig.ROOT_PATH) ? WebServerConfig.ENTRY_FILE : uri;
    }

    public static HttpRequest create(String requestLine) {
        String[] tokens = requestLine.split(" ");

        return new HttpRequest(
            new HttpVersion(tokens[2]),
            HttpMethod.valueOf(tokens[0]),
            tokens[1]);
    }

    public HttpMethod getMethod() {
        return method;
    }

    public String getUri() {
        return uri;
    }
}
