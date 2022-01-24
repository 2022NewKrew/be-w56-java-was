package http;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

public class Headers {

    private final Map<String, String> map;

    public Headers(Map<String, String> map) {
        this.map = Collections.unmodifiableMap(map);
    }

    public String get(String key) {
        return map.get(key);
    }

    public Set<Map.Entry<String, String>> entrySet() {
        return map.entrySet();
    }

    public static Headers contentType(String contentType) {
        return new Headers(Collections.singletonMap("Content-Type", contentType));
    }
}
