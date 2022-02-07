package app.http;

import java.util.LinkedHashMap;
import java.util.Map;

public class HttpHeader {
    private final Map<String, String> headers;

    public static HttpHeader of() {
        return new HttpHeader();
    }

    private HttpHeader() {
        headers = new LinkedHashMap<>();
    }

    public String get(String key) {
        return headers.getOrDefault(key, "");
    }

    public String get(String key, String defaultValue) {
        return headers.getOrDefault(key, defaultValue);
    }

    public void put(String key, String value) {
        headers.put(key, value);
    }

    public Map<String, String> header() {
        return headers;
    }
}
