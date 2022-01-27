package di;

import java.util.*;

public class BeanContainer {

    private final Map<Class<?>, List<Object>> beans = new HashMap<>();

    public void put(Class<?> clazz, Object bean) {
        putBeans(clazz, bean);
    }

    private void putBeans(Class<?> key, Object value) {
        beans.putIfAbsent(key, new ArrayList<>());
        beans.computeIfPresent(key, (k, v) -> {
            v.add(value);
            return v;
        });
    }

    public Object getFirst(Class<?> x) {
        return Optional.ofNullable(beans.get(x))
                .map(list -> list.get(0))
                .orElse(null);
    }

    public List<Object> getAll(Class<?> x) {
        return beans.get(x);
    }
}
