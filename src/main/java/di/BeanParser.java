package di;

import annotation.Bean;
import annotation.Inject;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class BeanParser {

    // TODO refactor
    public BeanContainer parse(Class<?>[] classes) {
        BeanContainer container = new BeanContainer();
        for (Class<?> clazz : classes) {
            Constructor<?> constructor = getInjectableConstructor(clazz);
            if (constructor == null) {
                continue;
            }
            container.put(clazz, new ClassInstantiator(constructor));
            for (Method m : clazz.getDeclaredMethods()) {
                if (m.getAnnotation(Bean.class) == null) {
                    continue;
                }
                container.put(m.getReturnType(), new Instantiator() {
                    @Override
                    public Class<?>[] getParameterTypes() {
                        return m.getParameterTypes();
                    }

                    @Override
                    public Object newInstance(Object[] parameters) throws InstantiationException, IllegalAccessException, InvocationTargetException {
                        return m.invoke(container.getFirst(clazz), parameters);
                    }
                });
            }
        }
        return container;
    }

    private Constructor<?> getInjectableConstructor(Class<?> clazz) {
        if (!clazz.isAnnotationPresent(Bean.class)) {
            return null;
        }
        Constructor<?> constructor = null;
        Constructor<?>[] constructors = clazz.getDeclaredConstructors();
        for (Constructor<?> c : constructors) {
            if (c.getParameterTypes().length == 0) {
                constructor = c;
            }
            Inject annotation = c.getAnnotation(Inject.class);
            if (annotation != null) {
                constructor = c;
                break;
            }
        }
        if (constructor == null) {
            throw new RuntimeException("No matching constructor for class " + clazz.getName());
        }
        return constructor;
    }
}
