package http.header;

import java.util.*;

public class HttpHeaders implements MultiValueMap<String, String> {

    public static final String ACCEPT = "Accept";
    public static final String CONTENT_TYPE = "Content-Type";
    public static final String CONTENT_LENGTH = "Content-Length";
    public static final String HOST = "Host";
    public static final String ORIGIN = "Origin";
    public static final String LOCATION = "Location";
    public static final String COOKIE = "Cookie";
    public static final String SET_COOKIE = "Set-Cookie";

    private final Map<String, List<String>> headers;

    public HttpHeaders() {
        this.headers = new HashMap<>();
    }

    public HttpHeaders(List<String> headerString) {
        this();
        initHeader(headerString);
    }

    private void initHeader(List<String> headerString) {
        headerString.forEach(header -> {
            String[] split = header.split(": ");
            for (String value : split[1].split(",")) {
                add(split[0], value.trim());
            }
        });
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
