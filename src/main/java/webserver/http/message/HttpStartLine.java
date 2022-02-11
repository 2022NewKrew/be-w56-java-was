package webserver.http.message;

import webserver.http.message.values.HttpMethod;

public class HttpStartLine {
    private final HttpMethod method;
    private final String uri;
    private final String version;
    private final String queryStrings;

    public HttpStartLine(String method, String uri, String version) {
        this(method, uri, null, version);
    }

    public HttpStartLine(String method, String uri, String queryStrings, String version) {
        this(HttpMethod.find(method), uri, queryStrings, version);
    }

    public HttpStartLine(HttpMethod method, String uri, String queryStrings, String version) {
        this.method = method;
        this.uri = uri;
        this.version = version;
        this.queryStrings = queryStrings;
    }

    public HttpMethod getMethod() {
        return method;
    }

    public String getUri() {
        return uri;
    }

    public String getVersion() {
        return version;
    }

    public String getQueryStrings() {
        return queryStrings;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(method).append(" ");
        sb.append(uri);
        if(queryStrings != null)
            sb.append("?").append(queryStrings);
        sb.append(" ");
        sb.append(version);
        return sb.toString();
    }
}
