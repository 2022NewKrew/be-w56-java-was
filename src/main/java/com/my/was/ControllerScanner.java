package com.my.was;

import com.my.was.container.handlermappings.annotation.Controller;
import com.my.was.container.handlermappings.annotation.RequestMapping;
import org.reflections.Reflections;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ControllerScanner {

    public Set<Class<?>> scan() {
        Reflections reflections = new Reflections(getClass().getPackageName());
        return reflections.getTypesAnnotatedWith(Controller.class);
//        return mappingController(reflections.getTypesAnnotatedWith(Controller.class));
    }

    private Map<String, Class<?>> mappingController(Set<Class<?>> controllers) {
        Map<String, Class<?>> controllerMap = new HashMap<>();
        for (Class<?> clazz : controllers) {
            mappingRequestMappingMethod(clazz, controllerMap);
        }

        return controllerMap;
    }

    private void mappingRequestMappingMethod(Class<?> clazz, Map<String, Class<?>> controllerMap) {
        for (Method method : clazz.getMethods()) {
            if (method.isAnnotationPresent(RequestMapping.class)) {
                RequestMapping annotation = method.getAnnotation(RequestMapping.class);
                controllerMap.put(annotation.value(), clazz);
            }
        }
    }
}
