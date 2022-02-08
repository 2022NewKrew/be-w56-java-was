package servlet.view;

import java.util.HashMap;
import java.util.Map;

public class Model {
    private final Map<String, Object> attributes;

    public Model() {
        attributes = new HashMap<>();
    }

    public void setAttribute(String key, Object attribute) {
        attributes.put(key, attribute);
    }

    public Object getAttribute(String key) {
        return attributes.get(key);
    }

    public boolean isEmpty() {
        return attributes.isEmpty();
    }
}
