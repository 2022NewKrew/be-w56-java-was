package framework.params;

import com.google.common.collect.Maps;

import java.util.Map;

public class Model {
    Map<String, String> model = Maps.newConcurrentMap();

    public void setAttributes(String key, String value) {
        model.put(key, value);
    }
}
