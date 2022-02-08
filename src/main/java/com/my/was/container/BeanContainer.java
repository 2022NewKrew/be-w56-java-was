package com.my.was.container;

import com.my.was.ControllerScanner;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class BeanContainer {

    private static BeanContainer beanContainer = new BeanContainer(new ControllerScanner());
    private Map<Class<?>, Object> beans = new HashMap<>();

    private BeanContainer(ControllerScanner controllerScanner) {
        for (Class<?> clazz : controllerScanner.scan()) {
            try {
                beans.put(clazz, clazz.getConstructor().newInstance());
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

    }

    public static BeanContainer getInstance() {
        return beanContainer;
    }

    public Object getBean(Class<?> clazz) {
        return beans.get(clazz);
    }

    public Set<Class<?>> getBeanTypes() {
        return beans.keySet();
    }
}
