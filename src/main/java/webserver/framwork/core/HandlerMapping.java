package webserver.framwork.core;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import org.reflections.Reflections;
import org.reflections.scanners.MethodAnnotationsScanner;
import org.reflections.scanners.Scanner;
import org.reflections.scanners.Scanners;
import webserver.framwork.http.request.HttpMethod;

import java.lang.reflect.Method;
import java.util.Set;

public class HandlerMapping {
    private static final HandlerMapping instance = new HandlerMapping();
    private static final String appPackage = "webserver.application";

    private final Table<HttpMethod, String, Method> handlerMethods = HashBasedTable.create();

    private HandlerMapping() {
        init();
    }

    public static HandlerMapping getInstance() {
        return instance;
    }

    public void init() {
        Reflections reflections = new Reflections(appPackage, Scanners.MethodsAnnotated);
        Set<Method> methods = reflections.getMethodsAnnotatedWith(RequestMapping.class);
        for (Method method : methods) {
            RequestMapping mapping = method.getAnnotation(RequestMapping.class);
            handlerMethods.put(mapping.method(), mapping.value(), method);
            System.out.println(mapping.method() + ", " + mapping.value());
        }
    }

    public Method getHandlerMethod(HttpMethod method, String url) {
        return handlerMethods.get(method, url);
    }
}
