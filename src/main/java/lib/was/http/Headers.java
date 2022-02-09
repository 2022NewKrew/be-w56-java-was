package lib.was.http;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Headers {

    public static final Headers EMPTY = new Headers(Collections.emptyMap());

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

    public Headers plus(String key, String value) {
        Map<String, String> map = Stream.concat(
                this.map.entrySet().stream().filter(x -> !x.getKey().equals(key)),
                Stream.of(new AbstractMap.SimpleEntry<>(key, value))
        ).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        return new Headers(map);
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
}
