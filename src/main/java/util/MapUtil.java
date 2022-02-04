package util;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class MapUtil {

    public static <K, V> Map<K, V> get(Class<K> k, Class<V> v) {
        return new HashMap<>();
    }

    public static Map<String, String> getEmptyCookieMap() {
        return Collections.emptyMap();
    }

    public static Map<String, Object> getEmptyModelMap() {
        return Collections.emptyMap();
    }
}
