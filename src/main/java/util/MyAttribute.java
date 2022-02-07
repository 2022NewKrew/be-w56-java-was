package util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class MyAttribute {

    Map<String, Object> info = new HashMap<>();

    public Object get(String key) {
        return info.get(key);
    }

    public void set(String key, Object value) {
        info.put(key, value);
    }


    public List<Object> getAll() {
        Set<String> keys = info.keySet();

        return keys.stream().map(k -> info.get(k)).collect(Collectors.toList());
    }

    public Map<String, Object> getInfo() {
        return info;
    }
}
