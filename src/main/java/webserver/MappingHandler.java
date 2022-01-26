package webserver;

import controller.Controller;
import http.GetMapping;
import http.HttpMethod;
import http.HttpRequest;
import http.PostMapping;
import http.Url;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.reflections.Reflections;
import webserver.resolver.ArgumentResolvers;

public class MappingHandler {

    private static final String CONTROLLER_PACKAGE = "controller";
    private static final Map<Url, Method> mappings = new HashMap<>();

    static {
        Set<Class<?>> controllers = new Reflections(CONTROLLER_PACKAGE)
            .getTypesAnnotatedWith(Controller.class);
        controllers.forEach(MappingHandler::registerMethod);
    }

    private static void registerMethod(Class<?> controllerClass) {
        Arrays.stream(controllerClass.getDeclaredMethods())
            .filter(method -> method.isAnnotationPresent(GetMapping.class))
            .forEach(method -> {
                GetMapping requestMapping = method.getAnnotation(GetMapping.class);
                mappings.put(new Url(HttpMethod.GET, requestMapping.value()), method);
            });
        Arrays.stream(controllerClass.getDeclaredMethods())
            .filter(method -> method.isAnnotationPresent(PostMapping.class))
            .forEach(method -> {
                PostMapping requestMapping = method.getAnnotation(PostMapping.class);
                mappings.put(new Url(HttpMethod.POST, requestMapping.value()), method);
            });
    }

    public static String invoke(HttpRequest httpRequest) throws Exception {
        Method method = mappings.get(httpRequest.getUrl());
        if (method == null) {
            return httpRequest.getUrl().getPath();
        }
        Object[] args = ArgumentResolvers.resolve(method, httpRequest);
        Object instance = method.getDeclaringClass().getDeclaredConstructor().newInstance();
        return (String) method.invoke(instance, args);
    }
}
