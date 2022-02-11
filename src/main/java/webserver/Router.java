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
import java.lang.reflect.Parameter;
import java.util.*;
import java.util.stream.Collectors;

public class Router {
    private static final Logger log = LoggerFactory.getLogger(Router.class);

    public static HttpResponse route(HttpRequest httpRequest) {
        String path = httpRequest.getPath();
        String requestMethod = httpRequest.getMethod();
        log.info("Path: {}, Request Method: {}", path, requestMethod);
        if (isStaticFile(path)) {
            return ResponseGenerator.generateStaticResponse(path);
        }

        Class<?> matchedController = matchController(path);
        try {
            return matchMethodAndInvoke(matchedController, httpRequest);
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

    private static Class<?> matchController(String path) {
        Reflections reflections = new Reflections("controller");
        Set<Class<?>> controllers = reflections.getTypesAnnotatedWith(Controller.class);
        return getMatchingController(controllers, path);
    }

    private static Class<?> getMatchingController(Set<Class<?>> classes, String path) {
        return classes.stream()
                .filter(c -> path.startsWith(c.getAnnotation(Controller.class).value()))
                .findFirst()
                .orElse(RootController.class);
    }

    private static HttpResponse matchMethodAndInvoke(
            Class<?> matchedController,
            HttpRequest httpRequest)
            throws Exception {
        String path = httpRequest.getPath();
        String requestMethod = httpRequest.getMethod();

        Method[] methods = matchedController.getMethods();
        Method method = getMatchingMethod(methods, path, requestMethod);
        List<Parameter> parameters = Arrays.stream(method.getParameters())
                .collect(Collectors.toUnmodifiableList());

        List<Object> args = new ArrayList<>();
        Map<String, String> httpParams = httpRequest.getParameters();
        parameters.forEach(p -> {
                    if (p.getName().equals("loggedin")) {
                        args.add(httpRequest.isLoggedIn());
                    } else {
                        args.add(httpParams.get(p.getName()));
                    }
                });
        
        return (HttpResponse) method.invoke(matchedController.getConstructor().newInstance(), args.toArray());
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
