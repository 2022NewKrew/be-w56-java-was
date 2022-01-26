package webserver.processor.handler.controller;

import http.HttpMethod;
import http.HttpRequest;
import http.HttpResponse;
import webserver.exception.InternalServerErrorException;
import webserver.processor.handler.Handler;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

class ControllerMethod implements Handler {

    private Controller controller;
    private Method method;
    private Request request;

    ControllerMethod(Controller controller, Method method, Request request) {
        this.controller = controller;
        this.method = method;
        this.request = request;
    }

    @Override
    public boolean supports(HttpRequest httpRequest) {
        HttpMethod method = request.method();
        String path = request.value();
        return path.equals(httpRequest.getPath()) && method.equals(httpRequest.getMethod());
    }

    @Override
    public HttpResponse handle(HttpRequest httpRequest) {
        HttpResponse response = null;
        try {
            response = (HttpResponse) method.invoke(controller, httpRequest);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new InternalServerErrorException(e.getClass().getName(), e);
        }
        return response;
    }
}
