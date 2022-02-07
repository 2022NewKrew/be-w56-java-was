package webserver.core;

import org.reflections.Reflections;
import org.reflections.scanners.Scanners;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.annotations.RequestMapping;
import webserver.context.Url;
import webserver.handler.MethodHandler;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class HandlerMapping {
    private static final Logger log = LoggerFactory.getLogger(HandlerMapping.class);

    private static HandlerMapping handlerMapping;

    private ComponentManager componentManager;

    private Map<Url, Url> urlPatternByUrlPattern;

    private Map<Url, MethodHandler> methodHandlerMapByUrlPattern;

    private HandlerMapping(ComponentManager componentManager, Map<Url, Url> urlPatternByUrlPattern, Map<Url, MethodHandler> methodHandlerMapByUrlPattern) {
        this.componentManager = componentManager;
        this.urlPatternByUrlPattern = urlPatternByUrlPattern;
        this.methodHandlerMapByUrlPattern = methodHandlerMapByUrlPattern;
    }

    public static HandlerMapping getInstance(ComponentManager componentManager) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        if (handlerMapping == null) {
            handlerMapping = new HandlerMapping(componentManager, new HashMap<>(), new HashMap<>());
            handlerMapping.registryAllUrl();
        }
        return handlerMapping;
    }

    private void registryAllUrl() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        Reflections annotationReflector = new Reflections("webserver.annotations");
        Reflections methodReflector = new Reflections("myspring",  Scanners.MethodsAnnotated);
        Set<Class<?>> requestAnnotations = annotationReflector.getTypesAnnotatedWith(RequestMapping.class);

        for(Class<?> requestAnnotation: requestAnnotations) {
            Set<Method> methods = methodReflector.getMethodsAnnotatedWith((Class<Annotation>) requestAnnotation);
            for(Method method: methods) {
                updateUrlMapByMethod((Class<Annotation>) requestAnnotation, method);
            }
        }
    }

    private String[] getValueOrPathByAnnotation(Annotation annotation) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        if (annotation == null) return new String[]{""};
        String[] values = (String[]) annotation.getClass().getDeclaredMethod("value").invoke(annotation);
        String[] paths = (String[]) annotation.getClass().getDeclaredMethod("path").invoke(annotation);
        return values.length == 0 ? paths : values;
    }

    private void updateUrlMapByMethod(Class<Annotation> annotationClass, Method method) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        log.debug(method.toString());
        Class<?> clazz = method.getDeclaringClass();
        MethodHandler methodHandler = MethodHandler.of(componentManager.getObjectByClazz(clazz), method);
        String[] baseUrls = getValueOrPathByAnnotation(clazz.getAnnotation(RequestMapping.class));
        for (String baseUrl : baseUrls){
            Annotation methodAnnotation = method.getAnnotation(annotationClass);
            String[] urls = getValueOrPathByAnnotation(methodAnnotation);
            for (String url : urls){
                Url urlPattern = Url.of(Url.HttpMethod.valueOf(methodAnnotation.annotationType()), baseUrl + url, true);
                log.debug(String.format("Url mapping  %s : %s", urlPattern.getUrl(), methodHandler.getMethod().toString()));
                methodHandlerMapByUrlPattern.put(urlPattern, methodHandler);
                urlPatternByUrlPattern.put(urlPattern,urlPattern);
            }
        }
    }

    public Url getUrlPatternBy(Url url) {
        if (!urlPatternByUrlPattern.containsKey(url)) throw new IllegalArgumentException(String.format("Invalid Url! %s %s", url.getHttpMethod(), url.getUrl()));
        return urlPatternByUrlPattern.get(url);
    }

    public MethodHandler getMethodHandlerBy(Url url) {
        if (!methodHandlerMapByUrlPattern.containsKey(url)) throw new IllegalArgumentException(String.format("Invalid Url! %s %s", url.getHttpMethod(), url.getUrl()));
        return methodHandlerMapByUrlPattern.get(url);
    }


}
