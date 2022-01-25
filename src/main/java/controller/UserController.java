package controller;

import db.DataBase;
import service.LoginService;
import util.ModelAndView;
import util.UrlFormatter;
import webserver.HttpRequest;
import webserver.config.WebConst;

import java.io.DataOutputStream;
import java.util.HashMap;
import java.util.Map;

public class UserController extends Controller{
    private static LoginService loginService = new LoginService();


    public UserController() {
        baseUrl = "/user";

        runner.put("GET/user/form", (req, res) -> {
            String viewName = UrlFormatter.format(req.getRequestUri());
            return new ModelAndView(viewName, 200);
        });

        runner.put("GET/user/create", (req, res) -> {
            loginService.join(req.getParams());
            String viewName = UrlFormatter.format(WebConst.HOME);
            return new ModelAndView(viewName, 200);
        });

        runner.put("POST/user/create", (req, res) -> {
            loginService.join(req.getParams());
            String viewName = UrlFormatter.format(WebConst.HOME);
            return new ModelAndView(viewName, 302);
        });
    }


}
