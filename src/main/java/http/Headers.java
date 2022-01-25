package http;

import java.util.Collections;
import java.util.Map;
import java.util.Objects;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Headers headers = (Headers) o;
        return Objects.equals(map, headers.map);
    }

    @Override
    public int hashCode() {
        return Objects.hash(map);
    }

    public static Headers contentType(ContentType contentType) {
        return new Headers(Collections.singletonMap("Content-Type", contentType.getContentType()));
    }
}
