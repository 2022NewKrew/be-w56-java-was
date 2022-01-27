package webserver.domain;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Cookie {
    private final Map<String, String> datas;
    private final Set<String> addedKeys;

    public Cookie() {
        datas = new HashMap<>();
        addedKeys = new HashSet<>();
    }

    public void addExistingData(Map<String, String> datas) {
        this.datas.putAll(datas);
    }

    public void addData(String key, String value) {
        datas.put(key, value);
        addedKeys.add(key);
    }

    public String getData(String key) {
        return datas.get(key);
    }

    public Set<String> getKeys() {
        return datas.keySet();
    }

    public Set<String> getAddedKeys() {
        return addedKeys;
    }
}
