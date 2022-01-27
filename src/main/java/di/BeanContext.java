package di;

import di.annotation.Bean;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

@Bean
public class BeanContext {
    private final Map<Class<?>, Object> context = new HashMap<>();

    private BeanContext() {
    }

    private static class BeanContextWrapper {
        private static final BeanContext INSTANCE = new BeanContext();
    }

    public static BeanContext getInstance() {
        return BeanContextWrapper.INSTANCE;
    }

    public void make(String packageName) throws InvocationTargetException, InstantiationException, IllegalAccessException {
        final Set<Class<?>> annotatedClasses = BeanScanner.findBean(packageName);

        final Set<Class<?>> visited = new HashSet<>();

        for (Class<?> annotatedClass : annotatedClasses) {
            if (context.containsKey(annotatedClass)) {
                continue;
            }
            visited.add(annotatedClass);
            make(annotatedClass, visited);
            visited.remove(annotatedClass);
        }
    }

    private Object make(Class<?> cur, Set<Class<?>> visited) throws InvocationTargetException, InstantiationException, IllegalAccessException {
        final Constructor<?>[] declaredConstructors = cur.getDeclaredConstructors();

        if (declaredConstructors.length != 1) {
            throw new RuntimeException();
        }

        final Constructor<?> constructor = declaredConstructors[0];
        final Class<?>[] parameterTypes = constructor.getParameterTypes();

        if (parameterTypes.length == 0) {
            if (!hasBean(cur)) {
                register(constructor.newInstance());
            }
            return get(cur);
        }

        final Object[] parameters = new Object[parameterTypes.length];
        int pos = 0;
        for (Class<?> parameterType : parameterTypes) {
            if (visited.contains(parameterType)) {
                throw new RuntimeException();
            }

            if (hasBean(parameterType)) {
                final Object object = get(parameterType);
                parameters[pos++] = object;
                continue;
            }

            visited.add(parameterType);
            final Object object = make(parameterType, visited);
            parameters[pos++] = object;
            visited.remove(parameterType);
        }

        register(constructor.newInstance(parameters));

        return get(cur);
    }

    public <T> T get(Class<T> clazz) {
        final Object result = context.get(clazz);
        Objects.requireNonNull(result);

        if (!clazz.isInstance(result)) {
            throw new RuntimeException();
        }

        return (clazz.cast(result));
    }

    public boolean hasBean(Class<?> clazz) {
        return context.containsKey(clazz);
    }

    public void register(Object bean) {
        final Class<?> clazz = bean.getClass();

        if (context.containsKey(clazz)) {
            throw new RuntimeException();
        }

        context.put(clazz, bean);
    }
}
