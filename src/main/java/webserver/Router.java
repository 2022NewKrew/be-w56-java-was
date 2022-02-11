package webserver;

import annotation.Controller;
import annotation.RequestMapping;
import controller.RootController;
import exception.NoSuchPathException;
import exception.RequestMethodNotSupportedException;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.http.HttpRequest;
import webserver.http.HttpResponse;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Set;

public class Router {
    private static final Logger log = LoggerFactory.getLogger(Router.class);

    public static HttpResponse route(HttpRequest httpRequest) {
        String path = httpRequest.getPath();
        String requestMethod = httpRequest.getMethod();
        log.info("Path: {}, Request Method: {}", path, requestMethod);
        if (isStaticFile(path)) {
            log.info("static response");
            return ResponseGenerator.generateStaticResponse(path);
        }
        log.info("dynamic response");

        Reflections reflections = new Reflections();
        Set<Class<?>> controllers = reflections.getTypesAnnotatedWith(Controller.class);
        Class<?> matchedController = getMatchingController(controllers, path);
        try {
            Method[] methods = matchedController.getMethods();
            Method method = getMatchingMethod(methods, path, requestMethod);
            return (HttpResponse) method.invoke(matchedController.getConstructor().newInstance(), httpRequest);
        } catch (NoSuchPathException e) {
            log.error("NoSuchPathException occurred");
            return ResponseGenerator.generateResponse404();
        } catch (RequestMethodNotSupportedException e) {
            log.error("RequestMethodNotSupportedException occurred");
            return ResponseGenerator.generateResponse405();
        } catch (Exception e) {
            log.error("Unexpected exception: ", e);
            return ResponseGenerator.generateResponse500();
        }
    }

    private static Class<?> getMatchingController(Set<Class<?>> classes, String path) {
        return classes.stream()
                .filter(c -> path.startsWith(c.getAnnotation(Controller.class).value()))
                .findFirst()
                .orElse(RootController.class);
    }

    private static Method getMatchingMethod(Method[] methods, String path, String requestMethod) throws NoSuchPathException, RequestMethodNotSupportedException {
        try{
            // 지정한 path가 컨트롤러에 존재하는지 검사
            Arrays.stream(methods)
                    .filter(m -> path.equals(m.getAnnotation(RequestMapping.class).value()))
                    .findAny()
                    .orElseThrow(NoSuchPathException::new);
        } catch (Exception e) {
            throw new NoSuchPathException();
        }

        try {
            // 지정한 path와 request method가 컨트롤러에 존재하는지 검사
            return Arrays.stream(methods)
                    .filter(m -> path.equals(m.getAnnotation(RequestMapping.class).value()))
                    .filter(m -> requestMethod.equals(m.getAnnotation(RequestMapping.class).requestMethod()))
                    .findFirst()
                    .orElseThrow(RequestMethodNotSupportedException::new);
        } catch (Exception e) {
            throw new RequestMethodNotSupportedException();
        }
    }

    private static boolean isStaticFile(String path) {
        return path.contains(".");
    }
}
