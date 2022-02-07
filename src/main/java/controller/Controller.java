package controller;

import annotation.Auth;
import annotation.RequestMapping;
import util.Cookie;
import util.HttpMethod;
import util.HttpStatus;
import util.Message;
import exception.AuthorizationException;
import util.request.HttpRequestUtils;
import util.request.Request;
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
            String queryString = HttpRequestUtils.getQueryString(url);
            url = url.split("\\?")[0];
            parameters = HttpRequestUtils.parseQueryString(queryString);
        }
        String cookieString = request.getHeader("Cookie");
        Cookie cookie = new Cookie(cookieString);
        try {
            Method method = findMethod(httpMethod, url, cookie);
            return getResponse(method, parameters);
        } catch (AuthorizationException e) {
            Message message = new Message("로그인이 되어있지 않습니다.", "/user/login.html");
            return ResponseException.errorResponse(message, HttpStatus.UNAUTHORIZED);
        }
    }

    private Method findMethod(HttpMethod httpMethod, String url, Cookie cookie) throws AuthorizationException {
        Class<?> controllerClass = this.getClass();
        Method[] methods = controllerClass.getDeclaredMethods();
        for (Method method : methods) {
            RequestMapping requestMapping = method.getDeclaredAnnotation(RequestMapping.class);
            if (requestMapping == null) continue;
            if (!url.equals(requestMapping.url())) continue;
            if (!httpMethod.equals(requestMapping.method())) continue;
            Auth auth = method.getDeclaredAnnotation(Auth.class);
            if (auth != null && !cookie.getLogin()) throw new AuthorizationException();

            return method;
        }
        return null;
    }

    private Response getResponse(Method method, Map<String, String> parameters) throws IOException {
        try {
            method.setAccessible(true);
            return (Response) method.invoke(this, parameters);
        } catch (NullPointerException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
            return ResponseException.errorResponse(new Message(e.getMessage(), "/index.html"), HttpStatus.BAD_REQUEST);
        }
    }

}
