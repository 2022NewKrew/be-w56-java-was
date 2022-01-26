package infrastructure.model;

import java.util.Arrays;
import java.util.Collections;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class HttpHeader {

    private final Set<Pair> headers;

    public HttpHeader(Set<Pair> headers) {
        this.headers = headers;
    }

    public static HttpHeader of(Pair... pairs) {
        return new HttpHeader(
                Arrays.stream(pairs)
                        .collect(Collectors.toSet())
        );
    }

    public void addHeader(Pair pair) {
        headers.add(pair);
    }

    public Set<Pair> getHeaders() {
        return Collections.unmodifiableSet(headers);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HttpHeader that = (HttpHeader) o;
        return Objects.equals(headers, that.headers);
    }

    @Override
    public int hashCode() {
        return Objects.hash(headers);
    }

    @Override
    public String toString() {
        return "HttpHeader{" +
                "headers=" + headers +
                '}';
    }
}
