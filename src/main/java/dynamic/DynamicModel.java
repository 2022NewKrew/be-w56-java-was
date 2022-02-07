package dynamic;

import java.util.*;
import java.util.stream.Collectors;

public class DynamicModel {
    private final Map<String, Object> model;

    public DynamicModel() {
        model = new HashMap<>();
    }

    public void addAttribute(String key, Object value) {
        model.put(key, value);
    }

    public Object getAttribute(String key) {
        if (model.containsKey(key)) {
            return model.get(key);
        }
        System.out.println("input key: " + key);
        List<String> keyList = model.keySet()
                .stream()
                .collect(Collectors.toCollection(ArrayList::new));
        System.out.println("key List: " + keyList.toString());
        throw new NoSuchElementException();
    }
}
