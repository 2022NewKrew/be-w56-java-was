package servlet;

import java.util.HashMap;
import java.util.Map;

public class Model {
    private Map<String, Object> attributes;

    public Model() {
        attributes = new HashMap<>();
    }

    public void setAttribute(String key, Object attribute) {
        attributes.put(key, attribute);
    }

    public Object getAttribute(String key) {
        return attributes.get(key);
    }

    public boolean isEmpthy() {
        return attributes.isEmpty();
    }
}
