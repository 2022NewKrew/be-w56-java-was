package springmvc.frontcontroller;

import webserver.http.request.CustomHttpMethod;

import java.util.Objects;

public class HandleMappingKey {
    private String uri;
    private CustomHttpMethod method;

    public HandleMappingKey(String uri, CustomHttpMethod method) {
        this.uri = uri;
        this.method = method;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HandleMappingKey that = (HandleMappingKey) o;
        return Objects.equals(uri, that.uri) && method == that.method;
    }

    @Override
    public int hashCode() {
        return Objects.hash(uri, method);
    }
}
