package webserver.http;

import java.util.*;

public class HttpHeader {

    private final Map<String, List<String>> headers;

    public HttpHeader() {
        this.headers = new HashMap<>();
    }

    public HttpHeader(Map<String, List<String>> headers) {
        this.headers = headers;
    }

    public boolean containsKey(String key) {
        return headers.containsKey(key);
    }

    public List<String> getValues(String key) {
        return headers.get(key);
    }

    public void put(String key, String value) {
        List<String> values = headers.getOrDefault(key, new ArrayList<>());
        values.add(value);
        headers.put(key, values);
    }

    public Set<String> keySet() {
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
