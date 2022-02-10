package webserver;

import java.util.HashMap;
import java.util.Map;

public class Model {
    private final Map<String, Object> models = new HashMap<>();

    public void addAttribute(String attributeName, Object attributeValue) {
        models.put(attributeName, attributeValue);
    }

    public Object getAttribute(String attributeName) {
        return models.get(attributeName);
    }

    public boolean isEmpty() {
        return models.isEmpty();
    }
}
