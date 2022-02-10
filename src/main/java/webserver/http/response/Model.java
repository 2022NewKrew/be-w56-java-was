package webserver.http.response;

import java.util.HashMap;
import java.util.Map;

public class Model {
    private Map<String, Object> attribute = new HashMap<>();

    public Model() {
    }

    public void addAttribute(String key, Object value) {
        attribute.put(key, value);
    }

    public Object getAttribute(String key) {
        return attribute.get(key);
    }

    public boolean hasAttribute() {
        return !attribute.isEmpty();
    }

    public boolean isEmpty() {
        return attribute.isEmpty();
    }
}
