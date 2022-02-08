package webserver;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@NoArgsConstructor
@Getter
public class Model {

    private final Map<String, Object> data = new HashMap<>();

    public void addAttribute(String name, Object object) {
        data.put(name, object);
    }

    public void addAllAttribute(String name, List<?> objects) {
        data.put(name, objects);
    }

    public Object getAttribute(String key) {
        return data.get(key);
    }
}
