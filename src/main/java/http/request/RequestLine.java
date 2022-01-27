package http.request;

import http.HttpMethod;

public class RequestLine {

    private final HttpMethod method;
    private final String path;
    private final Queries queries;

    public RequestLine(HttpMethod method, String path, Queries queries) {
        this.method = method;
        this.path = path;
        this.queries = queries;
    }

    public HttpMethod getMethod() {
        return method;
    }

    public String getPath() {
        return path;
    }

    public Queries getQueries() {
        return queries;
    }
}
