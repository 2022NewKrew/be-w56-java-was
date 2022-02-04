package http;

import java.util.*;

public class MultiValueMap<K, V> {

    private Map<K, List<V>> map;

    public MultiValueMap() {
        this.map = new HashMap<>();
    }

    public void add(K key, V value) {
        List<V> values = map.getOrDefault(key, new ArrayList<>());
        values.add(value);
        if(!map.containsKey(key)) {
            map.put(key, values);
        }
    }

    public void addAll(K key, List<? extends V> values) {
        List<V> targetValues = map.getOrDefault(key, new ArrayList<>());
        targetValues.addAll(values);
        if(!map.containsKey(key)) {
            map.put(key, targetValues);
        }
    }

    public List<V> getAll(K key) {
        return map.getOrDefault(key, new ArrayList<>());
    }

    public Optional<V> getFirst(K key) {
        return map.getOrDefault(key, new ArrayList<>()).stream().findFirst();
    }

    public Set<Map.Entry<K, List<V>>> entrySet() {
        return map.entrySet();
    }
}
