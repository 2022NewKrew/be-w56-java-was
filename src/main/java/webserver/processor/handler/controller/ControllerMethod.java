package webserver.processor.handler.controller;

import http.HttpMethod;
import http.HttpRequest;
import webserver.http.RequestEntity;
import webserver.http.ResponseEntity;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

class ControllerMethod {

    private final Controller controller;
    private final Method method;
    private final Request request;
    private final Type methodArgumentGenericInnerType;

    ControllerMethod(Controller controller, Method method, Request request) {
        this.controller = controller;
        this.method = method;
        this.method.setAccessible(true);
        ParameterizedType type = (ParameterizedType) method.getParameters()[0].getParameterizedType();
        this.methodArgumentGenericInnerType = type.getActualTypeArguments()[0];
        this.request = request;
    }

    public Type getMethodArgumentGenericInnerType() {
        return methodArgumentGenericInnerType;
    }

    public boolean supports(HttpRequest httpRequest) {
        HttpMethod method = request.method();
        String path = request.value();
        return path.equals(httpRequest.getPath()) && httpRequest.equalsMethod(method);
    }

    public Method getMethod() {
        return method;
    }

    public ResponseEntity<?> handle(RequestEntity<?> requestEntity) throws Throwable {
        ResponseEntity<?> response = null;
        try {
            response = (ResponseEntity<?>) method.invoke(controller, requestEntity);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw e.getCause();
        }
        return response;
    }
}
