package annotation;

import org.reflections.Reflections;
import org.reflections.scanners.MethodAnnotationsScanner;
import webserver.http.HttpRequest;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Set;

public class AnnotationProcessor {
    private static final AnnotationProcessor instance = new AnnotationProcessor();

    private AnnotationProcessor(){}

    public static AnnotationProcessor getInstance(){
        return instance;
    }

    public Object requestMappingProcessor(HttpRequest request) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Reflections reflections = new Reflections("webserver.controller",
                new MethodAnnotationsScanner()
        );

        Set<Method> methods = reflections.getMethodsAnnotatedWith(RequestMapping.class);
        for (Method m : methods) {
            RequestMapping annotation = m.getAnnotation(RequestMapping.class);
            if (annotation.method() == request.getMethod() && annotation.url().equals(request.getUrl())) {
                Object controller = m.getDeclaringClass().getMethod("getInstance").invoke(null);
                return m.invoke(controller, request);
            }
        }

        return null;
    }
}
