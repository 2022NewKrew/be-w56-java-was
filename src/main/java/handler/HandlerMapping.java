package handler;

import annotation.Controller;
import http.request.HttpRequest;
import org.reflections.Reflections;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

public class HandlerMapping {
    private static final Set<HandlerMethod> HANDLER_METHODS;
    static{
        HANDLER_METHODS = new HashSet<>();
        Set<Class<?>> controllers = new Reflections(new ConfigurationBuilder()
                .setUrls(ClasspathHelper.forPackage("app")))
                .getTypesAnnotatedWith(Controller.class);

        for (Class<?> controller : controllers) {
            for (Method method : controller.getDeclaredMethods()) {
                HANDLER_METHODS.add(new HandlerMethod(controller, method));
            }
        }
    }

    public static HandlerMethod findHandlerMethodOf(HttpRequest rawHttpRequest) {
        for (HandlerMethod handlerMethod : HANDLER_METHODS) {
            if (handlerMethod.isHandleable(rawHttpRequest)) {
                return handlerMethod;
            }
        }
        return null;
    }
}
