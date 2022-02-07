package webserver.resolver;

import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.RequestHandler;
import webserver.annotation.AnnotationUtils;
import webserver.annotation.Controller;
import webserver.annotation.RequestMapping;
import webserver.annotation.RequestParam;
import webserver.domain.Cookie;
import webserver.domain.Model;
import webserver.domain.Request;
import webserver.domain.RequestMethod;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class ControllerResolver {
    private final Map<Method, Object> controllerForMethod;
    private final Map<Request, Method> methodForRequest;
    private final Reflections reflector;
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);
    private static final ControllerResolver instance = new ControllerResolver();

    public static ControllerResolver getInstance() {
        return instance;
    }

    public String resolveRequest(Request request, Model model) throws InvocationTargetException, IllegalAccessException {
        if (request.getAcceptType() != null) return request.getUrl();

        Method method = methodForRequest.get(request);
        return (String) method.invoke(controllerForMethod.get(method), resolveParameter(method, request, model));
    }

    private Object[] resolveParameter(Method method, Request request, Model model) {
        Parameter[] parameters = method.getParameters();
        return Arrays.stream(parameters).map(p -> {
            if (p.isAnnotationPresent(RequestParam.class))
                return request.getBody().get(p.getAnnotation(RequestParam.class).value());
            if (p.getParameterizedType().equals(Model.class))
                return model;
            if (p.getParameterizedType().equals(Cookie.class))
                return request.getCookie();
            return null;
        }).toArray();
    }

    private ControllerResolver() {
        reflector = new Reflections("controller");
        controllerForMethod = getMappingControllerForMethod();
        methodForRequest = getMappingMethodForRequest();
    }

    private Map<Method, Object> getMappingControllerForMethod() {
        Map<Method, Object> ret = new HashMap<>();
        Set<Object> controllerInstances = reflector.getTypesAnnotatedWith(Controller.class).stream().map(c -> {
            try {
                return c.getConstructor().newInstance();
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }).collect(Collectors.toSet());
        for (Object instance : controllerInstances) {
            for (Method method : instance.getClass().getDeclaredMethods()) ret.put(method, instance);
        }

        return ret;
    }

    private Map<Request, Method> getMappingMethodForRequest() {
        Map<Request, Method> ret = new HashMap<>();
        controllerForMethod.forEach((method, controller) -> {
            if (AnnotationUtils.isMethodHasAnnotation(method, RequestMapping.class)) {
                RequestMethod requestMethod = RequestMethod.valueOf(AnnotationUtils.getRequestMethodOfController(method));
                String requestURL = AnnotationUtils.getRequestURLOfController(method);
                ret.put(new Request(requestMethod, requestURL), method);
            }
        });
        return ret;
    }
}