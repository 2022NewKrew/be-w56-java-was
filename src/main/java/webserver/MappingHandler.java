package webserver;

import com.google.common.collect.Maps;
import controller.Controller;
import http.GetMapping;
import http.HttpMethod;
import http.HttpRequest;
import http.HttpResponse;
import http.PostMapping;
import http.Url;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import org.reflections.Reflections;
import webserver.resolver.ArgumentResolvers;

public class MappingHandler {

    private static final String CONTROLLER_PACKAGE = "controller";
    private static final Map<Url, Method> mappings = Maps.newHashMap();

    static {
        Set<Class<?>> controllers = new Reflections(CONTROLLER_PACKAGE)
            .getTypesAnnotatedWith(Controller.class);
        controllers.forEach(MappingHandler::registerMethod);
    }

    private MappingHandler() {
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

    public static void invoke(HttpRequest httpRequest, HttpResponse httpResponse, Model model)
        throws Exception {
        Method method = mappings.get(httpRequest.getUrl());
        if (method == null) {
            httpResponse.ok(httpRequest.getUrl().getPath());
            return;
        }
        Object[] args = ArgumentResolvers.resolve(method, httpRequest, httpResponse, model);
        Object instance = method.getDeclaringClass().getDeclaredConstructor().newInstance();
        method.invoke(instance, args);
    }
}
