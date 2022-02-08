package model;

import java.util.HashMap;
import java.util.Map;

public class Model {

    private final Map<String, Object> objectMap;

    private Model() {
        this.objectMap = new HashMap<>();
    }

    public static Model create() {
        return new Model();
    }

    public void addAttribute(String key, Object value) {
        objectMap.put(key, value);
    }

    public Object getAttribute(String key) {
        return objectMap.get(key);
    }
}
