package di;

import annotation.Bean;
import org.slf4j.Logger;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

public class BeanParser {

    private final Logger logger;

    public BeanParser(Logger logger) {
        this.logger = logger;
    }

    public BeanContainer parse(Class<?>[] classes) {
        BeanContainer container = new BeanContainer();
        for (Class<?> clazz : classes) {
            parseBeans(container, clazz);
        }
        return container;
    }

    private void parseBeans(BeanContainer container, Class<?> clazz) {
        if (!clazz.isAnnotationPresent(Bean.class)) {
            return;
        }
        Object typeBean;
        Constructor<?> constructor;
        try {
            constructor = clazz.getDeclaredConstructor();
        } catch (NoSuchMethodException e) {
            logger.error("No default constructor for class {}", clazz.getName());
            return;
        }
        try {
            typeBean = constructor.newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            logger.error("Error while instantiating class {}", clazz.getName(), e);
            return;
        }
        container.put(clazz, typeBean);
        parseMethodBeans(container, clazz, typeBean);
    }

    private void parseMethodBeans(BeanContainer container, Class<?> clazz, Object instance) {
        for (Method method : clazz.getDeclaredMethods()) {
            parseMethodBean(container, instance, method);
        }
    }

    private void parseMethodBean(BeanContainer container, Object instance, Method method) {
        if (!method.isAnnotationPresent(Bean.class)) {
            return;
        }
        Class<?>[] parameters = method.getParameterTypes();
        Object[] arguments = Arrays.stream(parameters).map(container::getFirst).toArray();
        Object methodBean;
        try {
            methodBean = method.invoke(instance, arguments);
        } catch (IllegalAccessException | InvocationTargetException e) {
            logger.error(
                    "Error while invoking method {} on {}",
                    method.getName(),
                    instance.getClass().getName(),
                    e
            );
            return;
        }
        container.put(method.getReturnType(), methodBean);
    }
}
