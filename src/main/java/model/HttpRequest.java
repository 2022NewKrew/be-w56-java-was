package model;

import model.request.Body;
import model.request.Headers;
import model.request.HttpLocation;
import model.request.HttpMethod;

import java.util.Map;
import java.util.Objects;

public class HttpRequest {
    private final HttpMethod method;
    private final HttpLocation location;
    private final ValueMap parameters;
    private final Headers headers;
    private final Body body;

    public HttpRequest(
            final HttpMethod method,
            final HttpLocation location,
            final ValueMap parameters,
            final Headers headers,
            final Body body
    )
    {
        this.method = Objects.requireNonNull(method);
        this.location = Objects.requireNonNull(location);
        this.parameters = Objects.requireNonNull(parameters);
        this.headers = Objects.requireNonNull(headers);
        this.body = Objects.requireNonNull(body);
    }

    public HttpMethod getMethod() {
        return method;
    }

    public HttpLocation getHttpLocation() {
        return location;
    }

    public Map<String, String> getParameterMap() {
        return parameters.getMap();
    }

    public Headers getHeader() {
        return headers;
    }

    public String getBody() {
        return body.get();
    }
}
