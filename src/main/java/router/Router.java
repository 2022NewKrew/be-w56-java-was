package router;

import annotation.Controller;
import annotation.RequestMapping;
import model.Request;
import model.Response;
import org.reflections.Reflections;

import java.lang.reflect.Method;
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

        // urlPath와 매칭되는 controller 서칭
        Class<?> subControllerClass = annotated.stream()
                .filter(c -> urlPath.startsWith(c.getAnnotation(Controller.class).value()))
                .findFirst()
                .orElseThrow(ClassNotFoundException::new);

        Method[] methods = subControllerClass.getDeclaredMethods();

        //찾은 컨트롤러에서 method 서칭
        Method method = Arrays.stream(methods)
                .filter(c -> urlPath.equals(c.getAnnotation(RequestMapping.class).value()))
                .filter(c -> requestMethod.equals(c.getAnnotation(RequestMapping.class).requestMethod()))
                .findFirst()
                .orElseThrow(IllegalAccessException::new);
        return (Response) method.invoke(subControllerClass.getConstructor().newInstance(),request);
    }

    private static boolean isStatic(String urlPath) {
        return urlPath.contains(".");
    }

    private static Response defaultRouting(Request request) {
        return Response.of(request, request.getUrlPath());
    }
}
