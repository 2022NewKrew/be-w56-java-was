package webserver;

import http.HttpMethod;

import java.util.Objects;

public class RequestMappingInfo {
    private final String uri;
    private final HttpMethod method;

    public RequestMappingInfo(String uri, HttpMethod method) {
        this.uri = uri;
        this.method = method;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        RequestMappingInfo other = (RequestMappingInfo) obj;
        return uri.equals(other.uri)
                && method.equals(other.method);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uri, method);
    }

    @Override
    public String toString() {
        return "RequestMappingInfo [uri=" + uri +
                ", method=" + method +
                "]";
    }
}
