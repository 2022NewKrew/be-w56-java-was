package framework.params;

import com.google.common.collect.Maps;

import java.util.Map;

public class Model {
    Map<String, Object> model = Maps.newConcurrentMap();

    public void setAttributes(String key, Object value) {
        model.put(key, value);
    }

    public Object getAttributes(String key) {
        return model.get(key);
    }
}
