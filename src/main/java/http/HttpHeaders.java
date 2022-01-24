package http;

import http.map.MultiValueMap;

import java.util.*;
import java.util.stream.Collectors;

public class HttpHeaders implements MultiValueMap<String, String> {

    public static final String ACCEPT = "Accept";
    private final Map<String, List<String>> headers;

    public HttpHeaders() {
        this(new HashMap<>());
    }

    public HttpHeaders(Map<String, List<String>> headers) {
        this.headers = headers;
    }

    @Override
    public String getFirst(String key) {
        List<String> values = headers.get(key);
        return values != null && !values.isEmpty() ? values.get(0) : null;
    }

    @Override
    public void add(String key, String value) {
        List<String> values = headers.computeIfAbsent(key, k -> new ArrayList<>(1));
        values.add(value);
    }

    public HttpHeaders addAll(HttpHeaders headers) {
        for (Entry<String, List<String>> entry : headers.entrySet()) {
            String key = entry.getKey();
            List<String> values = entry.getValue();
            for (String value : values) {
                this.add(key, value);
            }
        }
        return this;
    }

    @Override
    public int size() {
        return headers.size();
    }

    @Override
    public boolean isEmpty() {
        return headers.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        return headers.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return headers.containsValue(value);
    }

    @Override
    public List<String> get(Object key) {
        return headers.get(key);
    }

    @Override
    public List<String> put(String key, List<String> value) {
        return headers.put(key, value);
    }

    @Override
    public List<String> remove(Object key) {
        return headers.remove(key);
    }

    @Override
    public void putAll(Map<? extends String, ? extends List<String>> m) {
        headers.putAll(m);
    }

    @Override
    public void clear() {
        headers.clear();
    }

    @Override
    public Set<String> keySet() {
        return headers.keySet();
    }

    @Override
    public Collection<List<String>> values() {
        return headers.values();
    }

    @Override
    public Set<Entry<String, List<String>>> entrySet() {
        return headers.entrySet();
    }
}
