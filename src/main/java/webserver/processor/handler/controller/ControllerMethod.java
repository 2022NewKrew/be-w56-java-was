package webserver.processor.handler.controller;

import http.HttpMethod;
import http.HttpRequest;
import webserver.exception.InternalServerErrorException;
import webserver.http.RequestEntity;
import webserver.http.ResponseEntity;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

class ControllerMethod {

    private Controller controller;
    private Method method;
    private Request request;
    private Type methodArgumentGenericInnerType;

    ControllerMethod(Controller controller, Method method, Request request) {
        this.controller = controller;
        this.method = method;
        method.setAccessible(true);
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
        return path.equals(httpRequest.getPath()) && method.equals(httpRequest.getMethod());
    }

    public Method getMethod() {
        return method;
    }

    public ResponseEntity<?> handle(RequestEntity<?> requestEntity) {
        ResponseEntity<?> response = null;
        try {
            response = (ResponseEntity<?>) method.invoke(controller, requestEntity);
        } catch (IllegalAccessException | InvocationTargetException e) {
            Throwable cause = e.getCause();
            throw new InternalServerErrorException(cause.getClass().getName(), cause);
        }
        return response;
    }
}
