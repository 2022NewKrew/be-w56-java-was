package webserver;

import com.google.common.collect.Maps;
import java.util.Map;

public class Model {

    private final Map<String, Object> attributes = Maps.newHashMap();

    public void setAttribute(String key, Object value) {
        attributes.put(key, value);
    }

    public Object getAttribute(String key) {
        return attributes.get(key);
    }

    public boolean contains(String key) {
        return attributes.containsKey(key);
    }

    public boolean isEmpty() {
        return attributes.isEmpty();
    }
}
