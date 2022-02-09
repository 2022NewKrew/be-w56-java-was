package lib.was.di;

import com.google.common.primitives.Primitives;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.stream.Collectors;

public class BeanContainer {

    private final Map<Class<?>, List<Instantiator>> beans = new HashMap<>();

    public void put(Class<?> clazz, Instantiator instantiator) {
        Class<?> wrapped = Primitives.wrap(clazz);
        for (Class<?> interfaze : clazz.getInterfaces()){
            put(interfaze, instantiator);
        }
        beans.putIfAbsent(wrapped, new ArrayList<>());
        beans.computeIfPresent(wrapped, (k, v) -> {
            v.add(instantiator);
            return v;
        });
    }

    public Object getFirst(Class<?> x) {
        Class<?> wrapped = Primitives.wrap(x);
        if (!beans.containsKey(wrapped)) {
            putDefaultConstructor(wrapped);
        }
        return Optional.ofNullable(beans.get(wrapped))
                .map(list -> list.get(0))
                .map(this::instantiate)
                .orElse(null);
    }

    public List<Object> getAll(Class<?> x) {
        Class<?> wrapped = Primitives.wrap(x);
        if (!beans.containsKey(wrapped)) {
            putDefaultConstructor(wrapped);
        }
        return beans.getOrDefault(wrapped, Collections.emptyList())
                .stream()
                .map(this::instantiate)
                .collect(Collectors.toList());
    }

    private void putDefaultConstructor(Class<?> x) {
        try {
            Constructor<?> constructor = x.getDeclaredConstructor();
            put(x, new ClassInstantiator(constructor));
        } catch (NoSuchMethodException e) {
            throw new RuntimeException("No default constructor found for " + x.getName(), e);
        }
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
