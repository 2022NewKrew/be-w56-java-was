package http.request;

import util.Pair;

import java.util.List;

public class HttpRequestHeaders {
    private final List<Pair> headers;

    public HttpRequestHeaders(List<Pair> headers) {
        this.headers = headers;
    }
}
