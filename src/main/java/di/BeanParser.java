package di;

import annotation.Bean;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

public class BeanParser {

    public BeanContainer parse(Class<?>[] classes)
            throws InvocationTargetException, NoSuchMethodException, IllegalAccessException, InstantiationException {
        BeanContainer container = new BeanContainer();
        for (Class<?> clazz : classes) {
            parseBeans(container, clazz);
        }
        return container;
    }

    private void parseBeans(BeanContainer container, Class<?> clazz)
            throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, InstantiationException {
        if (!clazz.isAnnotationPresent(Bean.class)) {
            return;
        }
        Object typeBean = clazz.getDeclaredConstructor(new Class<?>[] {}).newInstance();
        container.put(clazz, typeBean);
        parseMethodBeans(container, clazz, typeBean);
    }

    private void parseMethodBeans(BeanContainer container, Class<?> clazz, Object instance)
            throws InvocationTargetException, IllegalAccessException {
        for (Method method : clazz.getDeclaredMethods()) {
            parseMethodBean(container, instance, method);
        }
    }

    private void parseMethodBean(BeanContainer container, Object instance, Method method)
            throws InvocationTargetException, IllegalAccessException {
        if (!method.isAnnotationPresent(Bean.class)) {
            return;
        }
        Class<?>[] parameters = method.getParameterTypes();
        Object[] arguments = Arrays.stream(parameters).map(container::getFirst).toArray();
        Object methodBean = method.invoke(instance, arguments);
        container.put(method.getReturnType(), methodBean);
    }
}
