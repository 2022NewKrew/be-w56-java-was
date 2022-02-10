package webserver.http.message;

import webserver.http.message.values.HttpMethod;

public class HttpRequest {
    private final HttpMethod method;
    private final String uri;
    private final String queryStrings;
    private final String version;
    private final HttpHeader header;
    private final HttpBody body;

    public HttpRequest(HttpStartLine startLine, HttpHeader header) {
        this(startLine, header, null);
    }

    public HttpRequest(HttpStartLine startLine, HttpHeader header, HttpBody body) {
        this.method = startLine.getMethod();
        this.uri = startLine.getUri();
        this.queryStrings = startLine.getQueryStrings();
        this.version = startLine.getVersion();

        this.header = header;
        this.body = body;
    }

    public HttpMethod getMethod() {
        return method;
    }

    public String getUri() {
        return uri;
    }

    public String getQueryStrings() {
        return queryStrings;
    }

    public String getVersion() {
        return version;
    }

    public HttpHeader getHeader() {
        return header;
    }

    public HttpBody getBody() {
        return body;
    }
}
