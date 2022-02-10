package template;

import java.util.HashMap;
import java.util.Map;

public class Model {

    private final Map<String, Object> map;

    public Model() {
        this.map = new HashMap<>();
    }

    public void addAttributes(String key, Object value) {
        this.map.put(key, value);
    }

    public boolean isEmpty() {
        return map.isEmpty();
    }

    public Map<String, Object> getMap() {
        return map;
    }

}
