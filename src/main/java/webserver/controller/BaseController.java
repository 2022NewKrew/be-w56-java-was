package webserver.controller;

import annotation.GetMapping;
import annotation.PostMapping;
import lombok.extern.slf4j.Slf4j;
import org.reflections.Reflections;
import org.reflections.scanners.MethodAnnotationsScanner;
import webserver.web.Parameters;
import webserver.web.request.Request;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

@Slf4j
public abstract class BaseController implements Controller {

    private final Map<String, Method> getHandlers = new HashMap<>();
    private final Map<String, Method> postHandlers = new HashMap<>();

    public BaseController() {
        Reflections reflections = new Reflections("webserver.controller", new MethodAnnotationsScanner());

        reflections.getMethodsAnnotatedWith(GetMapping.class)
                .forEach(method -> {
                    getHandlers.put(method.getAnnotation(GetMapping.class).url(), method);
                });
        reflections.getMethodsAnnotatedWith(PostMapping.class)
                .forEach(method -> {
                    postHandlers.put(method.getAnnotation(PostMapping.class).url(), method);
                });
    }

    @Override
    public final Object handle(Request request, Parameters requiredData) throws InvocationTargetException, IllegalAccessException {
        if (request.getMethod().toString().equals("GET")) {
            return handleGetMethod(request, requiredData);
        }
        if (request.getMethod().toString().equals("POST")) {
            return handlePostMethod(request, requiredData);
        }
        return null;
    }

    private Object handlePostMethod(Request request, Parameters requiredData) throws IllegalAccessException, InvocationTargetException {
        Method method = postHandlers.get(request.getUrl().getUrl());
        return runMethod(method, requiredData);
    }

    private Object handleGetMethod(Request request, Parameters requiredData) throws IllegalAccessException, InvocationTargetException {
        Method method = getHandlers.get(request.getUrl().getUrl());
        return runMethod(method, requiredData);
    }

    private Object runMethod(Method method, Parameters requiredData) throws IllegalAccessException, InvocationTargetException {
        List<Object> parameters = new ArrayList<>();
        Arrays.stream(method.getParameters()).forEach(param -> {
            parameters.add(requiredData.inquireData(param.getName(), param.getType()));
        });
        return method.invoke(this, parameters.toArray());
    }
}
