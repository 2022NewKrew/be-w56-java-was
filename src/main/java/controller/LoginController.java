package controller;

import http.HttpRequest;
import http.HttpResponse;
import service.UserService;
import webserver.HttpMethod;

import java.util.Map;

public class LoginController implements Controller {

    private final UserService userService = new UserService();

    @Override
    public void service(HttpRequest request, HttpResponse response) {
        HttpMethod httpMethod = request.getRequestLine().getHttpMethod();

        if (httpMethod.isGET()) {
            doGet(request, response);
        } else {
            doPost(request, response);
        }
    }

    @Override
    public void doGet(HttpRequest request, HttpResponse response) {
        response.forward("/user/login.html");
    }

    @Override
    public void doPost(HttpRequest request, HttpResponse response) {
        Map<String, String> params = request.getRequestParams().getParams();

        String redirectUrl;
        try {
            userService.login(params.get("userId"), params.get("password"));
            response.getHttpHeader().addHeaderParameter("Set-Cookie: logined=true; Path=/");
            redirectUrl = "/";
        } catch (RuntimeException e) {
            response.getHttpHeader().addHeaderParameter("Set-Cookie: logined=false; Path=/");
            redirectUrl = "/users/loginFail";
        }
        response.sendRedirect302Header(redirectUrl);
    }
}
