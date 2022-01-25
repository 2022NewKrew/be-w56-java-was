package http.response;

import util.Pair;

import java.util.List;

public class HttpResponseHeaders {
    private List<Pair> headers;

    public HttpResponseHeaders(List<Pair> headers) {
        this.headers = headers;
    }

    public HttpResponseHeaders() {}
}
