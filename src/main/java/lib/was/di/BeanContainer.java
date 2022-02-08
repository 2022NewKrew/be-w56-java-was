package lib.was.di;

import com.google.common.primitives.Primitives;

import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.stream.Collectors;

public class BeanContainer {

    private final Map<Class<?>, List<Instantiator>> beans = new HashMap<>();

    public void put(Class<?> clazz, Instantiator instantiator) {
        Class<?> wrapped = Primitives.wrap(clazz);
        beans.putIfAbsent(wrapped, new ArrayList<>());
        beans.computeIfPresent(wrapped, (k, v) -> {
            v.add(instantiator);
            return v;
        });
    }

    public Object getFirst(Class<?> x) {
        Class<?> wrapped = Primitives.wrap(x);
        return Optional.ofNullable(beans.get(wrapped))
                .map(list -> list.get(0))
                .map(this::instantiate)
                .orElse(null);
    }

    public List<Object> getAll(Class<?> x) {
        return beans.getOrDefault(x, Collections.emptyList())
                .stream()
                .map(this::instantiate)
                .collect(Collectors.toList());
    }

    private Object instantiate(Instantiator instantiator) {
        Object[] parameters = Arrays.stream(instantiator.getParameterTypes())
                .map(this::getFirst)
                .toArray();
        try {
            return instantiator.newInstance(parameters);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException("Constructor mismatch occurred", e);
        }
    }
}
