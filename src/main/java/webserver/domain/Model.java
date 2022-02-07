package webserver.domain;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Model {
    private Map<String, Object> attributes;

    public Model() {
        attributes = new HashMap<>();
    }

    public void addAttribute(String key, Object object) {
        attributes.put(key, object);
    }

    public Object getAttribute(String key) {
        return attributes.get(key);
    }

    public Set<String> getKeys() {
        return attributes.keySet();
    }

    public boolean isEmpty() {
        return attributes.isEmpty();
    }
}
