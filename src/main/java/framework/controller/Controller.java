package framework.controller;

import framework.util.annotation.RequestMapping;
import framework.webserver.HttpRequestHandler;
import framework.webserver.HttpResponseHandler;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Locale;

public interface Controller {
    default Object process(HttpRequestHandler request, HttpResponseHandler response) throws Exception {
        Class<?> currentClass = getClass();

        Controller currentInstance = (Controller) currentClass.getMethod("getInstance").invoke(null);
        Method method = findMethod(request.getUri(), request.getRequestMethod(), currentClass);

        try {
            return method.invoke(currentInstance, request, response);
        } catch (IllegalArgumentException e1) {
            try {
                return method.invoke(currentInstance, request);
            } catch (IllegalArgumentException e2) {
                return method.invoke(currentInstance);
            }
        }
    }

    default Method findMethod(String uri, String requestMethod, Class<?> currentClass) {
        return Arrays.stream(currentClass.getDeclaredMethods())
                .filter(m -> m.isAnnotationPresent(RequestMapping.class))
                .filter(m -> {
                    RequestMapping requestPath = m.getAnnotation(RequestMapping.class);
                    return uri.endsWith(requestPath.value()) &&
                            requestPath.requestMethod().toUpperCase(Locale.ROOT).equals(requestMethod);
                })
                .findFirst()
                .orElseThrow();
    }
}
