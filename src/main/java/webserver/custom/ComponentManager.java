package webserver.custom;

import annotations.Component;
import org.reflections.Reflections;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Parameter;
import java.util.*;

public class ComponentManager {

    private static ComponentManager componentManager;

    private Map<Class<?>, Object> objectMap;

    private ComponentManager() {
        objectMap = new HashMap<>();
    }

    public static ComponentManager of() throws InvocationTargetException, InstantiationException, IllegalAccessException, NoSuchMethodException {
        if (componentManager == null) {
            componentManager = new ComponentManager();
            componentManager.registryComponents();
        }
        return componentManager;
    }

    private void registryComponents() throws InvocationTargetException, InstantiationException, IllegalAccessException, NoSuchMethodException {
        Reflections annotationReflector = new Reflections("annotations");
        Reflections objReflector = new Reflections("users", "article", "reply", "main");
        Set<Class<?>> beanAnnotations = annotationReflector.getTypesAnnotatedWith(Component.class);

        for(Class<?> beanAnnotation: beanAnnotations) {
            Set<Class<?>> clazzs = objReflector.getTypesAnnotatedWith((Class<Annotation>) beanAnnotation);
            for(Class<?> clazz: clazzs) {
                updateObjectMapByClazz(clazz);
            }
        }
    }

    private void updateObjectMapByClazz(Class<?> clazz) throws InvocationTargetException, InstantiationException, IllegalAccessException, NoSuchMethodException {
        if (objectMap.containsKey(clazz)) return;
        Constructor[] constructors = clazz.getDeclaredConstructors();
        if (constructors.length == 0) return;
        Constructor constructor = constructors[0];
        List<Object> args = new ArrayList<>();
        for(Parameter parameter : constructor.getParameters()) {
            Class<?> targetClazz = parameter.getType();
            if (!objectMap.containsKey(targetClazz)) {
                updateObjectMapByClazz(targetClazz);
            }
            args.add(objectMap.get(targetClazz));
        }
        Object object = constructor.newInstance(args.toArray());
        for(Class<?> interfaceClazz : clazz.getInterfaces()) {
            objectMap.put(interfaceClazz, object);
        }
        objectMap.put(clazz, object);
    }

    public Object getObjectByClazz(Class<?> clazz) {
        if (!objectMap.containsKey(clazz)) throw new IllegalArgumentException("Invalid clazz!");
        return objectMap.get(clazz);
    }

}
