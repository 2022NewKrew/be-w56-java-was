package framework.handler;

import webserver.request.HttpRequest;

import java.lang.reflect.Method;

// (reflection 용)application.controller 의 method, Controller 객체, (match 용) HTTP Method, path 를 가지고 있음
public class HandlerMethod {

    private Object bean;
    private Method handleMethod;
    private String httpMethod;
    private String path;

    public HandlerMethod(Object bean, Method handleMethod, String httpMethod, String path) {
        this.bean = bean;
        this.handleMethod = handleMethod;
        this.httpMethod = httpMethod;
        this.path = path;
    }

    public boolean matchRequest(HttpRequest req) {
        return httpMethod.equals(req.getMethod()) && path.equals(req.getPath());
    }

    public Object getBean() {
        return bean;
    }

    public Method getHandleMethod() {
        return handleMethod;
    }

    public String getHttpMethod() {
        return httpMethod;
    }

    public String getPath() {
        return path;
    }
}
