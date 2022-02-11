package router;

import annotation.Controller;
import annotation.RequestMapping;
import model.Request;
import model.Response;
import org.reflections.Reflections;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.Set;

public class Router {
    private Router() {
        throw new IllegalStateException("Utility class");
    }

    public static Response routing(Request request) throws Exception {
        String urlPath = request.getUrlPath();
        String requestMethod = request.getHttpMethod();
        if(isStatic(urlPath)) {
            return defaultRouting(request);
        }

        Reflections reflections = new Reflections("controller");
        Set<Class<?>> annotated = reflections.getTypesAnnotatedWith(Controller.class);
        Class<?> subControllerClass = getMatchingControllerClass(annotated,urlPath);
        Method[] methods = subControllerClass.getDeclaredMethods();
        Method method = getMatchingMethod(methods, urlPath, requestMethod);
        return (Response) method.invoke(subControllerClass.getConstructor().newInstance(),request);
    }

    private static Class<?> getMatchingControllerClass(Set<Class<?>> annotated, String urlPath) throws ClassNotFoundException {
        return annotated.stream()
                .filter(c -> urlPath.startsWith(c.getAnnotation(Controller.class).value()))
                .findFirst()
                .orElseThrow(ClassNotFoundException::new);
    }

    private static Method getMatchingMethod(Method[] methods, String urlPath, String requestMethod) throws IllegalAccessException {
        return Arrays.stream(methods)
                .filter(c -> urlPath.equals(c.getAnnotation(RequestMapping.class).value()))
                .filter(c -> requestMethod.equals(c.getAnnotation(RequestMapping.class).requestMethod()))
                .findFirst()
                .orElseThrow(IllegalAccessException::new);
    }

    private static boolean isStatic(String urlPath) {
        return urlPath.contains(".");
    }

    private static Response defaultRouting(Request request) throws IOException {
        return Response.of(request, request.getUrlPath(), Files.readAllBytes(new File("./webapp" + request.getUrlPath()).toPath()));
    }
}
