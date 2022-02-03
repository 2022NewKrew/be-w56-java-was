package webserver.model;

import com.google.common.collect.Maps;

import java.util.Map;

public class Model {
    private final Map<String, Object> map;

    public Model() {
        this.map = Maps.newHashMap();
    }

    public Model addAttribute(String attributeName, Object attributeValue) {
        map.put(attributeName, attributeValue);
        return this;
    }

    public Object getAttribute(String attributeName) {
        return map.get(attributeName);
    }
}
