package http.header;

import java.util.List;
import java.util.Map;

public interface MultiValueMap<K, V> extends Map<K, List<V>> {
    V getFirst(K key);
    void add(K key, V value);
}
