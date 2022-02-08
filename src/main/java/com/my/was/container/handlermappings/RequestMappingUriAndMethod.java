package com.my.was.container.handlermappings;

import com.my.was.http.request.HttpMethod;

import java.util.Objects;

public class RequestMappingUriAndMethod {

    private final String path;
    private final HttpMethod httpMethod;

    public RequestMappingUriAndMethod(String path, HttpMethod httpMethod) {
        this.path = path;
        this.httpMethod = httpMethod;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RequestMappingUriAndMethod that = (RequestMappingUriAndMethod) o;
        return Objects.equals(path, that.path) && httpMethod == that.httpMethod;
    }

    @Override
    public int hashCode() {
        return Objects.hash(path, httpMethod);
    }
}
