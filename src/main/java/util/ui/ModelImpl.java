package util.ui;

import java.util.HashMap;
import java.util.Map;

public class ModelImpl implements Model{

    private final Map<String, Object> data;

    public ModelImpl(){
        data = new HashMap<>();
    }

    @Override
    public Object getAttribute(String key) {
        return data.get(key);
    }

    @Override
    public void addAttribute(String key, Object value) {
        data.put(key, value);
    }


}
