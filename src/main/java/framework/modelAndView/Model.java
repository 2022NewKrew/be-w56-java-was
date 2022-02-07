package framework.modelAndView;

import java.util.Map;

public class Model {

    private Map<String, Object> attribute;

    public void addAttribute(String key, Object value) {
        attribute.put(key, value);
    }
}
