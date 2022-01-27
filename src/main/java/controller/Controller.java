package controller;

import annotation.RequestMapping;
import util.HttpMethod;
import util.request.HttpRequestUtils;
import util.request.Request;
import util.request.RequestLine;
import util.response.Response;
import util.response.ResponseException;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

public class Controller {

    public Response view(Request request, String url) throws IOException {
        HttpMethod httpMethod = request.getLine().getMethod();
        Map<String, String> parameters = null;
        if (httpMethod.equals(HttpMethod.POST)) {
            parameters = HttpRequestUtils.parseQueryString(request.getBody());
        }
        if (httpMethod.equals(HttpMethod.GET)) {
            String queryString = url.split("\\?")[1];
            url = url.split("\\?")[0];
            parameters = HttpRequestUtils.parseQueryString(queryString);
        }
        Method method = findMethod(httpMethod,url);
        return getResponse(method,parameters);
    }

    private Method findMethod(HttpMethod httpMethod, String url) {
        Class<?> controllerClass = this.getClass();
        Method[] methods = controllerClass.getDeclaredMethods();
        for (Method method : methods) {
            RequestMapping requestMapping = method.getDeclaredAnnotation(RequestMapping.class);
            if (requestMapping != null && url.equals(requestMapping.url()) && httpMethod.equals(requestMapping.method())) {
                return method;
            }
        }
        return null;
    }

    private Response getResponse(Method method, Map<String, String> parameters) throws IOException {
        try {
            method.setAccessible(true);
            return (Response) method.invoke(this,parameters);
        } catch (NullPointerException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
            return ResponseException.badRequestResponse();
        }
    }

}
