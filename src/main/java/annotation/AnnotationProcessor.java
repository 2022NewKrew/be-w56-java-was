package annotation;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import org.reflections.Reflections;
import org.reflections.scanners.MethodAnnotationsScanner;
import webserver.http.HttpMethod;
import webserver.http.HttpRequest;
import webserver.http.HttpResponse;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Set;

public class AnnotationProcessor {
    private static final AnnotationProcessor instance = new AnnotationProcessor();
    private static Table<HttpMethod, String, Method> requestMap;
    private static final String CONTROLLER_LOCATION = "webserver.controller";

    private AnnotationProcessor(){
        requestMap = HashBasedTable.create();
        Reflections reflections = new Reflections(CONTROLLER_LOCATION,
                new MethodAnnotationsScanner()
        );

        Set<Method> methods = reflections.getMethodsAnnotatedWith(RequestMapping.class);
        for (Method m : methods) {
            RequestMapping annotation = m.getAnnotation(RequestMapping.class);
            requestMap.put(annotation.method(), annotation.url(), m);
        }
    }

    public static AnnotationProcessor getInstance(){
        return instance;
    }

    public Object requestMappingProcessor(HttpRequest request, HttpResponse response) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        if(requestMap.contains(request.getMethod(), request.getUrl())){
            Method method = requestMap.get(request.getMethod(), request.getUrl());
            Object controller = method.getDeclaringClass().getMethod("getInstance").invoke(null);

            return method.invoke(controller, request, response);
        }

        return null;
    }
}
