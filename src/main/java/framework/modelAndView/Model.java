package framework.modelAndView;

import java.util.HashMap;
import java.util.Map;

public class Model {

    private Map<String, Object> attribute = new HashMap<>();

    public void addAttribute(String key, Object value) {
        attribute.put(key, value);
    }

    public Object getInAttribute(String key) {
        return attribute.get(key);
    }
}
