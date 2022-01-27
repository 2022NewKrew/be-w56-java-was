package webserver.http;

import java.util.HashMap;
import java.util.Map;

public class HttpHeader {

    private final Map<String, String> map;

    public HttpHeader() {
        this.map = new HashMap<>();
    }

    public HttpHeader set(String key, MimeSubtype value) {
        return set(key, value.toString());
    }

    public HttpHeader set(String key, int value) {
        return set(key, String.valueOf(value));
    }

    public HttpHeader set(String key, String value) {
        map.put(key, value);
        return this;
    }

    public Map<String, String> getMap() {
        return map;
    }
}
