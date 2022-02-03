package http.response;

import java.util.HashMap;
import java.util.Map;

public class Model {
    private Map<String, String> attrMap = new HashMap<>();

    public void addAttribute(String key, String value) {
        attrMap.put(key, value);
    }

    public String getAttr(String key) {
        return attrMap.get(key);
    }

    public boolean isEmpty() {
        return attrMap.isEmpty();
    }
}
