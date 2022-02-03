package webserver;

import annotation.GetMapping;
import annotation.PostMapping;
import lombok.extern.slf4j.Slf4j;
import org.reflections.Reflections;
import org.reflections.scanners.MethodAnnotationsScanner;
import webserver.controller.Controller;
import webserver.web.request.Request;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class RequestMapper {

    private static final RequestMapper requestMapper = new RequestMapper();
    private final Map<String, Controller> handlers = new HashMap<>();

    private RequestMapper() {
        Reflections reflections = new Reflections("webserver", new MethodAnnotationsScanner());
        reflections.getMethodsAnnotatedWith(GetMapping.class).forEach(method -> {
            try {
                handlers.put(method.getAnnotation(GetMapping.class).url(), (Controller) method.getDeclaringClass().getMethod("getInstance").invoke(null));
            } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                e.printStackTrace();
            }
        });
        reflections.getMethodsAnnotatedWith(PostMapping.class).forEach(method -> {
            try {
                handlers.put(method.getAnnotation(PostMapping.class).url(), (Controller) method.getDeclaringClass().getMethod("getInstance").invoke(null));
            } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                e.printStackTrace();
            }
        });
    }

    public static RequestMapper getInstance() {
        return requestMapper;
    }

    public Controller mapping(Request request) {
        log.debug("request URL : {} {}", request.getMethod().toString(), request.getUrl().getUrl());
        return handlers.get(request.getUrl().getUrl());
    }
}
