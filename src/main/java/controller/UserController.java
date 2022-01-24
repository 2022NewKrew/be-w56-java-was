package controller;

import db.DataBase;
import service.LoginService;
import util.UrlFormatter;
import webserver.HttpRequest;
import webserver.config.WebConst;

import java.io.DataOutputStream;
import java.util.HashMap;
import java.util.Map;

public class UserController implements Controller{
    private Map<String, Controller> handler = new HashMap<>();
    private static LoginService loginService = new LoginService();


    @Override
    public boolean isSupport(String url) {
        return url.startsWith("/user");
    }

    @Override
    public String execute(HttpRequest httpRequest, DataOutputStream dos) {
        if(httpRequest.getMethod().equals("GET")) {
            return get(httpRequest);
        }
        return "";
    }

    private String get(HttpRequest httpRequest) {
        if(httpRequest.getRequestUri().startsWith("/user/form")) {
            return UrlFormatter.format(httpRequest.getRequestUri());
        }
        if(httpRequest.getRequestUri().startsWith("/user/create")) {
            loginService.join(httpRequest.getParams());
            return UrlFormatter.format(WebConst.HOME);
        }
        return "";
    }
}
