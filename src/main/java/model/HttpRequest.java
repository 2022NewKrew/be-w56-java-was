package model;

import model.request.Headers;
import model.request.HttpLocation;
import model.request.HttpMethod;
import util.HttpRequestUtils;

import java.util.List;
import java.util.Objects;

public class HttpRequest {
    private final HttpMethod method;
    private final HttpLocation location;
    private final Headers headers;

    public HttpRequest(
            final HttpMethod method,
            final HttpLocation location,
            final Headers headers
    )
    {
        this.method = Objects.requireNonNull(method);
        this.location = Objects.requireNonNull(location);
        this.headers = Objects.requireNonNull(headers);
    }

    public HttpMethod getMethod() {
        return method;
    }

    public HttpLocation getHttpLocation() {
        return location;
    }

    public List<HttpRequestUtils.Pair> getHeaderList() {
        return headers.getList();
    }
}
