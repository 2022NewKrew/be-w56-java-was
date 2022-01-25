package http;

import util.HttpRequestUtils;

import java.util.ArrayList;
import java.util.List;

public class HttpHeaders {
    private final List<HttpRequestUtils.Pair> headers = new ArrayList<>();

    public void add(String headerString) {
        HttpRequestUtils.Pair header = HttpRequestUtils.parseHeader(headerString);
        if (header != null) {
            headers.add(header);
        }
    }

    public String get(String key) {
        HttpRequestUtils.Pair header = headers.stream()
                .filter(h -> h.getKey().equals(key))
                .findFirst()
                .orElse(null);
        if (header == null) {
            return null;
        }
        return header.getValue();
    }

    public int size() {
        return headers.size();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof HttpHeaders)) {
            return false;
        }
        HttpHeaders other = (HttpHeaders) obj;
        return headers.equals(other.headers);
    }

    @Override
    public String toString() {
        return headers.toString();
    }
}
