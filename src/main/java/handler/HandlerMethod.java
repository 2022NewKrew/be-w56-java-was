package handler;

import annotation.RequestMapping;
import exception.InternalErrorException;
import http.request.HttpRequest;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Objects;

public class HandlerMethod {
    private final Class<?> handlerType;
    private final Method method;

    public HandlerMethod(Class<?> handlerType, Method method) {
        this.handlerType = handlerType;
        this.method = method;
    }

    public boolean isHandleable(HttpRequest request) {
        if (!method.isAnnotationPresent(RequestMapping.class)) {
            return false;
        }

        RequestMapping annotation = method.getAnnotation(RequestMapping.class);

        return request.getHttpMethod().equals(annotation.method()) && request.getUri().equals(annotation.uri());
    }

    public HandlerResult invoke(HttpRequest httpRequest) {
        try {
            return (HandlerResult) method.invoke(getHandler(), httpRequest);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new InternalErrorException(e.getMessage());
        }
    }

    private Object getHandler() {
        try {
            return handlerType.getConstructor().newInstance();
        } catch (NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
            throw new InternalErrorException(e.getMessage());
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HandlerMethod that = (HandlerMethod) o;
        return Objects.equals(handlerType, that.handlerType) && Objects.equals(method, that.method);
    }

    @Override
    public int hashCode() {
        return Objects.hash(handlerType, method);
    }
}
