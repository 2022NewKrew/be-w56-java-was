package webserver.method;

import annotation.CookieValue;
import annotation.RequestBody;
import http.HttpRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpRequestUtils;
import webserver.HandlerAdapter;
import webserver.ModelAndView;
import webserver.SingletonBeanRegistry;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Map;

public class RequestMappingHandlerAdapter implements HandlerAdapter {
    private static final Logger log = LoggerFactory.getLogger(RequestMappingHandlerAdapter.class);

    @Override
    public boolean supports(Object handler) {
        return handler instanceof RequestMappingHandler;
    }

    @Override
    public ModelAndView handle(HttpRequest request, Object handler) {
        RequestMappingHandler mappingHandler = (RequestMappingHandler) handler;
        Object controller = SingletonBeanRegistry.getBean(mappingHandler.getBeanName());
        Method method = mappingHandler.getMethod();
        Object[] args = getParameterValuesForMethod(request, method);
        try {
            return (ModelAndView) method.invoke(controller, args);
        } catch (IllegalAccessException | InvocationTargetException e) {
            log.error("Failed to invoke request mapping method of " + method.getName());
        }
        return null;
    }

    private Object[] getParameterValuesForMethod(HttpRequest request, Method method) {
        Parameter[] parameters = method.getParameters();
        Object[] args = new Object[parameters.length];
        for (int i = 0; i < parameters.length; ++i) {
            args[i] = getParameterValueForRequest(request, parameters[i]);
        }
        return args;
    }

    private Object getParameterValueForRequest(HttpRequest request, Parameter parameter) {
        if (parameter.isAnnotationPresent(RequestBody.class)) {
            return getParameterValueForRequestBody(request.getBody(), parameter);
        }
        if (parameter.isAnnotationPresent(CookieValue.class)) {
            return getParameterValueForCookieValue(request.getCookies(), parameter);
        }
        return null;
    }

    private Object getParameterValueForRequestBody(String body, Parameter parameter) {
        if (parameter.getType().equals(String.class)) {
            return body;
        }
        if (parameter.getType().isAssignableFrom(Map.class)) {
            return HttpRequestUtils.parseQueryString(body);
        }
        throw new RuntimeException("Invalid method type for request body");
    }

    private Object getParameterValueForCookieValue(Map<String, String> cookies, Parameter parameter) {
        CookieValue cookieValue = parameter.getAnnotation(CookieValue.class);
        return cookies.get(cookieValue.value());
    }
}
