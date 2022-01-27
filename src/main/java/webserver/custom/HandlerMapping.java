package webserver.custom;

import annotations.*;
import org.reflections.Reflections;
import org.reflections.scanners.Scanners;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.context.Url;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class HandlerMapping {
    private static final Logger log = LoggerFactory.getLogger(HandlerMapping.class);

    private static HandlerMapping handlerMapping;

    private ComponentManager componentManager;

    private Map<Url, MethodHandler> urlMap;

    private HandlerMapping(ComponentManager componentManager) {
        this.componentManager = componentManager;
        this.urlMap = new HashMap<>();
    }

    public static HandlerMapping of(ComponentManager componentManager) throws InvocationTargetException, IllegalAccessException {
        if (handlerMapping == null) {
            handlerMapping = new HandlerMapping(componentManager);
            handlerMapping.registryUrl();
        }
        return handlerMapping;
    }

    private void registryUrl() throws InvocationTargetException, IllegalAccessException {
        Reflections annotationReflector = new Reflections("annotations");
        Reflections methodReflector = new Reflections("users", "article", "reply", "main",  Scanners.MethodsAnnotated);
        Set<Class<?>> requestAnnotations = annotationReflector.getTypesAnnotatedWith(RequestMapping.class);

        for(Class<?> requestAnnotation: requestAnnotations) {
            Set<Method> methods = methodReflector.getMethodsAnnotatedWith((Class<Annotation>) requestAnnotation);
            for(Method method: methods) {
                updateUrlMapByMethod((Class<Annotation>) requestAnnotation, method);
            }
        }
    }

    private void updateUrlMapByMethod(Class<Annotation> annotationClass, Method method) throws InvocationTargetException, IllegalAccessException {
        log.debug(method.toString());
        Class<?> clazz = method.getDeclaringClass();
        MethodHandler methodHandler = new MethodHandler(componentManager.getObjectByClazz(clazz), method);
        String baseUrl = clazz.getAnnotation(RequestMapping.class).value();
        if (Objects.equals(annotationClass, GetMapping.class)) {
            urlMap.put(Url.of(Url.Method.GET, baseUrl + method.getAnnotation(GetMapping.class).path()), methodHandler);
        }
        if (Objects.equals(annotationClass, PostMapping.class)) {
            urlMap.put(Url.of(Url.Method.POST, baseUrl + method.getAnnotation(PostMapping.class).path()), methodHandler);
        }
        if (Objects.equals(annotationClass, PutMapping.class)) {
            urlMap.put(Url.of(Url.Method.PUT, baseUrl + method.getAnnotation(PutMapping.class).path()), methodHandler);
        }
        if (Objects.equals(annotationClass, DeleteMapping.class)) {
            urlMap.put(Url.of(Url.Method.DELETE, baseUrl + method.getAnnotation(DeleteMapping.class).path()), methodHandler);
        }
    }

    public MethodHandler getMethodHandlerBy(Url url) {
        if (!urlMap.containsKey(url)) throw new IllegalArgumentException("Invalid Url!");
        return urlMap.get(url);
    }


}
