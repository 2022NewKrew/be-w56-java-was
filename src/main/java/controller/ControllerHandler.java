package controller;

import annotation.Controller;
import annotation.RequestMapping;
import controller.css.CssController;
import controller.view.ViewController;
import http.HttpHeader;
import http.HttpRequest;
import http.HttpResponse;
import http.HttpStatus;
import org.reflections.Reflections;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Set;

public class ControllerHandler {
    public static HttpResponse run(HttpRequest httpRequest) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, InstantiationException {
        String path = httpRequest.getRequestLine().getPath();
        if (path.startsWith("/css") || path.startsWith("/favicon.ico") || path.endsWith(".html") || path.startsWith("/fonts")) {
            return treatUnspecifiedRequest(httpRequest);
        }

        Set<Class<?>> typesAnnotatedWith = new Reflections(new ConfigurationBuilder()
                .setUrls(ClasspathHelper.forPackage("controller")))
                .getTypesAnnotatedWith(Controller.class);

        String method = httpRequest.getRequestLine().getMethod();

        for (Class<?> controller : typesAnnotatedWith) {
            for (Method classMethod : controller.getDeclaredMethods()) {
                if (classMethod.isAnnotationPresent(RequestMapping.class)) {
                    RequestMapping annotation = classMethod.getAnnotation(RequestMapping.class);
                    if (!isValidMethod(annotation, path, method)) {
                        continue;
                    }
                    Object routerObject = controller.getConstructor(HttpRequest.class).newInstance(httpRequest);
                    return (HttpResponse) classMethod.invoke(routerObject);
                }
            }
        }

        return new HttpResponse("HTTP/1.1", HttpStatus.NOT_FOUND, new HttpHeader());
    }

    private static boolean isValidMethod(RequestMapping requestMapping, String path, String method) {
        if (!requestMapping.method().equals(method)) {
            return false;
        }

        if (!requestMapping.value().equals(path)) {
            return false;
        }

        return true;
    }

    private static HttpResponse treatUnspecifiedRequest(HttpRequest httpRequest) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        String path = httpRequest.getRequestLine().getPath();
        Class<?> klass = null;

        if (path.startsWith("/css")) {
            klass = CssController.class;
        } else if (path.equals("/favicon.ico")) {
            klass = FaviconController.class;
        } else {
            klass = ViewController.class;
        }

        for (Method classMethod : klass.getDeclaredMethods()) {
            if (classMethod.isAnnotationPresent(RequestMapping.class)) {
                Object routerObject = klass.getConstructor(HttpRequest.class).newInstance(httpRequest);
                return (HttpResponse) classMethod.invoke(routerObject);
            }
        }

        return new HttpResponse("HTTP/1.1", HttpStatus.NOT_FOUND, new HttpHeader());
    }
}
