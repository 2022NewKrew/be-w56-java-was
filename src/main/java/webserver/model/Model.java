package webserver.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Model {

    private final Map<String, Object> attributes;

    public Model() {
        this.attributes = new HashMap<>();
    }

    public void addAttribute(String name, Object value) {
        attributes.put(name, value);
    }

    public Object getAttribute(String name) {
        return attributes.get(name);
    }

    public boolean hasAttribute(String name) {
        return attributes.containsKey(name);
    }

    public List<Object> getList(String name) {
        if (!hasAttribute(name)) {
            return new ArrayList<>();
        }
        Object value = getAttribute(name);
        if (!(value instanceof List)) {
            throw new ClassCastException("리스트 타입이 아닙니다.");
        }
        return (List<Object>) value;
    }

    public boolean isEmpty() {
        return attributes.isEmpty();
    }

}
