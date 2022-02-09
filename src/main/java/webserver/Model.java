package webserver;

import exception.ModelMapException;
import java.util.HashMap;

public class Model extends HashMap<String, Object> {

    public Model() {
    }

    public void addAttribute(String key, Object value) {
        if (key == null) {
            throw new ModelMapException("Model 의 key 값은 null 이 될 수 없습니다.");
        }
        put(key, value);
    }

    public Object getValue(String key) {
        if (!this.containsKey(key)) {
            throw new ModelMapException("Model 에 key(" + key + ")가 존재하지 않습니다.");
        }
        return this.get(key);
    }
}
