package webserver.http;

import java.io.BufferedReader;
import java.io.IOException;
import webserver.WebServerConfig;

public class HttpRequest {

    private final HttpVersion version;
    private final HttpMethod method;
    private final String uri;
    private final HttpHeaders trailingHeaders;
    private final HttpRequestParams params;

    public HttpRequest(HttpVersion version, HttpMethod method, String uri) {
        this(version, method, uri, new HttpRequestParams());
    }

    public HttpRequest(HttpVersion version, HttpMethod method, String uri,
        HttpRequestParams params) {
        this.version = version;
        this.method = method;
        this.uri = uri.equals(WebServerConfig.ROOT_PATH) ? WebServerConfig.ENTRY_FILE : uri;
        this.params = params;
        this.trailingHeaders = new HttpHeaders();
    }

    public static HttpRequest create(BufferedReader reader) throws IOException {
        String requestLine = reader.readLine();
        String[] requestLineTokens = requestLine.split(" ");

        HttpMethod method = HttpMethod.valueOf(requestLineTokens[0]);
        HttpVersion version = new HttpVersion(requestLineTokens[2]);
        String[] uriParamsTokens = requestLineTokens[1].split("\\?");

        if (uriParamsTokens.length == 1) {
            return new HttpRequest(version, method, uriParamsTokens[0]);
        }
        HttpRequestParams params = new HttpRequestParams(uriParamsTokens[1]);
        return new HttpRequest(version, method, uriParamsTokens[0], params);
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
