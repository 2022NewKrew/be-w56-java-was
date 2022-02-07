package http.request;

import http.HttpMethod;

public class RequestLine {

    private final HttpMethod method;
    private final URI uri;

    public RequestLine(HttpMethod method, URI uri) {
        this.method = method;
        this.uri = uri;
    }

    public HttpMethod getMethod() {
        return method;
    }

    public URI getUri() {
        return uri;
    }
}
