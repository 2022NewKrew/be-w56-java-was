package webserver.http.request;

import java.util.List;

import static util.HttpRequestUtils.Pair;

public class HttpRequestHeader {
    private final List<Pair> headers;

    public HttpRequestHeader(List<Pair> headers) {
        this.headers = headers;
    }

    public List<Pair> getHeaders() {
        return headers;
    }
}
