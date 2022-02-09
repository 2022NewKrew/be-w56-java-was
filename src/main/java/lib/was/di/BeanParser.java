package lib.was.di;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

public class BeanParser {

    public BeanContainer parse(Class<?>[] classes) {
        BeanContainer container = new BeanContainer();
        for (Class<?> clazz : classes) {
            Constructor<?> constructor = getInjectableConstructor(clazz);
            if (constructor == null) {
                continue;
            }
            container.put(clazz, new ClassInstantiator(constructor));
            putMethodInstantiators(container, clazz);
        }
        return container;
    }

    private void putMethodInstantiators(BeanContainer container, Class<?> clazz) {
        for (Method method : clazz.getDeclaredMethods()) {
            if (!method.isAnnotationPresent(Bean.class)) {
                continue;
            }
            Class<?> returnType = method.getReturnType();
            Instantiator instantiator = new MethodInstantiator(method, () -> container.getFirst(clazz));
            container.put(returnType, instantiator);
        }
    }

    private Constructor<?> getInjectableConstructor(Class<?> clazz) {
        if (!clazz.isAnnotationPresent(Bean.class)) {
            return null;
        }
        Constructor<?> constructor;
        try {
            constructor = clazz.getDeclaredConstructor();
        } catch (NoSuchMethodException ignored) {
            constructor = null;
        }
        Constructor<?>[] constructors = clazz.getDeclaredConstructors();
        for (Constructor<?> c : constructors) {
            if (!c.isAnnotationPresent(Inject.class)) {
                continue;
            }
            constructor = c;
            break;
        }
        if (constructor == null) {
            throw new RuntimeException("No matching constructor for class " + clazz.getName());
        }
        return constructor;
    }

}
