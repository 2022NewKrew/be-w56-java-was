package webserver.http;

import java.util.*;

public class HttpHeader {

    private final Map<Header, List<String>> headers;

    public HttpHeader() {
        this.headers = new HashMap<>();
    }

    public HttpHeader(Map<Header, List<String>> headers) {
        this.headers = headers;
    }

    public boolean containsKey(Header key) {
        return headers.containsKey(key);
    }

    public List<String> getValues(Header key) {
        return headers.get(key);
    }

    public void put(Header key, String value) {
        List<String> values = headers.getOrDefault(key, new ArrayList<>());
        values.add(value);
        headers.put(key, values);
    }

    public Set<Header> keySet() {
        return headers.keySet();
    }

    @Override
    public int hashCode() {
        return headers.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof HttpHeader)) {
            return false;
        }
        HttpHeader h = (HttpHeader) obj;
        return this.headers.equals(h.headers);
    }
}
