package webserver.http.request;

import java.util.HashMap;
import java.util.Objects;

public class HttpRequestHeader {
    private final HashMap<String, String> headers;

    public HttpRequestHeader(HashMap<String, String> headers) {
        this.headers = headers;
    }

    public HashMap<String, String> getHeaders() {
        return headers;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HttpRequestHeader that = (HttpRequestHeader) o;
        return Objects.equals(headers, that.headers);
    }

    @Override
    public int hashCode() {
        return Objects.hash(headers);
    }

    @Override
    public String toString() {
        return "HttpRequestHeader{" + "headers=" + headers + '}';
    }
}
