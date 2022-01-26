package handler;

import annotation.Controller;
import annotation.RequestMapping;
import http.request.HttpRequest;
import org.reflections.Reflections;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;

import java.lang.reflect.Method;
import java.util.Set;

public class HandlerMapping {
    private static final Set<Class<?>> CONTROLLERS;
    static{
        CONTROLLERS = new Reflections(new ConfigurationBuilder()
                .setUrls(ClasspathHelper.forPackage("app")))
                .getTypesAnnotatedWith(Controller.class);
    }

    public static HandlerMethod findHandlerMethodOf(HttpRequest httpRequest) {
        for (Class<?> controller : CONTROLLERS) {
            for (Method method : controller.getDeclaredMethods()) {
                if (isHandlerMatched(method, httpRequest)) {
                    return new HandlerMethod(controller, method);
                }
            }
        }
        return null;
    }

    private static boolean isHandlerMatched(Method method, HttpRequest request) {
        if (!method.isAnnotationPresent(RequestMapping.class)) {
            return false;
        }

        RequestMapping annotation = method.getAnnotation(RequestMapping.class);

        return annotation.method().equals(request.getHttpMethod()) && annotation.uri().equals(request.getHttpUri());
    }
}
