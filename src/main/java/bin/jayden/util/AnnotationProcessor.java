package bin.jayden.util;


import bin.jayden.annotation.Controller;
import bin.jayden.annotation.GetMapping;
import bin.jayden.annotation.PostMapping;
import bin.jayden.annotation.RequestMapping;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class AnnotationProcessor {
    private static final Set<Class<?>> controllers;
    private static final Map<Class<?>, Object> controllerInstanceMap;

    static {
        Reflections reflections = new Reflections("bin.jayden", new SubTypesScanner(false));
        controllers = reflections.getSubTypesOf(Object.class).stream().filter(aClass -> aClass.getAnnotation(Controller.class) != null).collect(Collectors.toSet());
        controllerInstanceMap = Collections.unmodifiableMap(controllers.stream().collect(Collectors.toMap(controller -> controller, controller -> {
            try {
                return controller.getConstructor().newInstance();
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                throw new NullPointerException(e.getMessage());
            }
        })));

    }


    public static Map<String, Method> getGetRoutingMap() {
        Map<String, Method> routingMap = new HashMap<>();
        for (Class<?> controller : controllers) {
            RequestMapping requestMapping = controller.getAnnotation(RequestMapping.class);
            String basePath = "";
            if (requestMapping != null)
                basePath = requestMapping.value();
            Method[] methods = controller.getMethods();
            for (Method method : methods) {
                GetMapping annotation = method.getAnnotation(GetMapping.class);
                if (annotation != null)
                    routingMap.put(basePath + annotation.value(), method);
            }
        }
        return routingMap;
    }

    public static Map<String, Method> getPostRoutingMap() {
        Map<String, Method> routingMap = new HashMap<>();
        for (Class<?> controller : controllers) {
            RequestMapping requestMapping = controller.getAnnotation(RequestMapping.class);
            String basePath = "";
            if (requestMapping != null)
                basePath = requestMapping.value();
            Method[] methods = controller.getMethods();
            for (Method method : methods) {
                PostMapping annotation = method.getAnnotation(PostMapping.class);
                if (annotation != null)
                    routingMap.put(basePath + annotation.value(), method);
            }
        }
        return routingMap;
    }

    public static Object getInstanceByClass(Class<?> c) {
        return controllerInstanceMap.get(c);
    }
}
