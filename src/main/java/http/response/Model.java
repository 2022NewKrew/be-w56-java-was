package http.response;

import java.util.HashMap;
import java.util.Map;

public class Model {
    private final Map<String, Object> attrMap = new HashMap<>();

    public void addAttribute(String key, Object value) {
        attrMap.put(key, value);
    }

    public Object getAttr(String key) {
        return attrMap.get(key);
    }

    public boolean isEmpty() {
        return attrMap.isEmpty();
    }
}
