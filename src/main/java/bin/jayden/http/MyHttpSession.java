package bin.jayden.http;

import java.util.HashMap;
import java.util.Map;

public class MyHttpSession {
    private final Map<String, Object> attributeMap = new HashMap<>();

    public void addAttribute(String key, Object value) {
        attributeMap.put(key, value);
    }

    public Object getAttribute(String key) {
        return attributeMap.get(key);
    }
}
