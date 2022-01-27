package infrastructure.model;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class HttpHeader {

    private final Map<String, String> headers;

    public HttpHeader(Map<String, String> headers) {
        this.headers = headers;
    }

    public static HttpHeader of(Pair... pairs) {
        Map<String, String> headers = new HashMap<>();
        Arrays.stream(pairs)
                .forEach(e -> headers.put(e.key, e.value));
        return new HttpHeader(headers);
    }

    public static Builder builder() {
        return new Builder();
    }

    public void addHeader(Pair pair) {
        headers.put(pair.key, pair.value);
    }

    public String getHeader(String key) {
        return headers.get(key);
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

    public Map<String, String> getHeaders() {
        return headers;
    }

    public static class Builder {
        private final Map<String, String> headers;

        private Builder() {
            this.headers = new HashMap<>();
        }

        public Builder setHeader(Pair pair) {
            this.headers.put(pair.key, pair.value);
            return this;
        }

        public HttpHeader build() {
            return new HttpHeader(this.headers);
        }
    }
}
