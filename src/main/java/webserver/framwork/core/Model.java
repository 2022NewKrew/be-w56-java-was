package webserver.framwork.core;

import java.util.HashMap;
import java.util.Map;

public class Model {
    Map<String, Object> model = new HashMap<>();

    public void addAttribute(String key, Object value) {
        model.put(key, value);
    }

    public Object getAttribute(String key) {
        return model.get(key);
    }
}
