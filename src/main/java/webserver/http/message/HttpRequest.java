package webserver.http.message;

import webserver.http.message.values.HttpMethod;

import java.util.Map;

public class HttpRequest {
    private final HttpMethod method;
    private final String uri;
    private final Map<String, String> queryStrings;
    private final String version;
    private final HttpHeader header;
    private final String body;
    private final Map<String, String> requestParams;

    public HttpRequest(HttpStartLine startLine, Map<String, String> queryStrings, HttpHeader header) {
        this(startLine, queryStrings, header, null, null);
    }

    public HttpRequest(HttpStartLine startLine, Map<String, String> queryStrings, HttpHeader header, String body, Map<String, String> requestParams) {
        this.method = startLine.getMethod();
        this.uri = startLine.getUri();
        this.queryStrings = queryStrings;
        this.version = startLine.getVersion();
        this.header = header;
        this.body = body;
        this.requestParams = requestParams;
    }

    public HttpMethod getMethod() {
        return method;
    }

    public String getUri() {
        return uri;
    }

    public Map<String, String> getQueryStrings() {
        return queryStrings;
    }

    public String getVersion() {
        return version;
    }

    public HttpHeader getHeader() {
        return header;
    }

    public String getBody() {
        return body;
    }

    public Map<String, String> getRequestParams() {
        return requestParams;
    }
}
