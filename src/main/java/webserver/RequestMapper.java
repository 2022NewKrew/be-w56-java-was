package webserver;

import annotation.RequestMapping;
import http.HttpRequest;
import http.HttpResponse;
import http.HttpStatus;
import util.HttpRequestUtils;
import util.IOUtils;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Set;

public class RequestMapper {
    private static final Set<Class<?>> controllers = IOUtils.findAllClassesInPackage("controller");
    private final HttpRequest request;

    public RequestMapper(HttpRequest request) {
        this.request = request;
    }

    public HttpResponse getResponse() {
        HttpResponse response;
        response = getResponseFromController();
        if (response != null) {
            return response;
        }
        response = getResponseFromStaticFile();
        if (response != null) {
            return response;
        }
        return new HttpResponse(HttpStatus.NotFound);
    }

    private HttpResponse getResponseFromController() {
        for (Class<?> controller : controllers) {
            Method annotatedMethod = getAnnotatedMethodFromClass(controller);
            if (annotatedMethod != null) {
                Object response = invokeMethod(controller, annotatedMethod);
                return (HttpResponse) response;
            }
        }
        return null;
    }

    private Method getAnnotatedMethodFromClass(Class<?> clazz) {
        Method[] methods = clazz.getDeclaredMethods();
        return Arrays.stream(methods)
                .filter(method -> method.isAnnotationPresent(RequestMapping.class))
                .filter(this::isAnnotationValid)
                .findFirst()
                .orElse(null);
    }

    private boolean isAnnotationValid(Method method) {
        RequestMapping mapping = method.getAnnotation(RequestMapping.class);
        return mapping.value().equals(request.getUri().getPath())
                && mapping.method().equals(request.getMethod());
    }

    private Object invokeMethod(Class<?> clazz, Method method) {
        Object object = SingletonRegistry.getSingleton(clazz.getName());
        String queryString = request.getUri().getQuery();
        try {
            return method.invoke(
                    object,
                    HttpRequestUtils.parseQueryString(queryString),
                    request.getBody());
        } catch (IllegalAccessException | InvocationTargetException e) {
            return null;
        }
    }

    private HttpResponse getResponseFromStaticFile() {
        Path filePath = Path.of("webapp", request.getUri().getPath());
        try {
            byte[] content = Files.readAllBytes(filePath);
            return new HttpResponse(content, HttpStatus.OK);
        } catch (IOException e) {
            return null;
        }
    }
}
