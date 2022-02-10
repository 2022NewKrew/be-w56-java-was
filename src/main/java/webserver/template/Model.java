package webserver.template;

import java.util.HashMap;
import java.util.Map;

public class Model {
    private final Map<String, Object> attributes = new HashMap<>();

    public static Model from() {
        return new Model();
    }

    private Model() {
    }

    public void addAttribute(String key, Object value) {
        attributes.put(key, value);
    }

    public Object getAttribute(String key) {
        return attributes.get(key);
    }

    public boolean isEmpty() {
        return attributes.isEmpty();
    }
}
