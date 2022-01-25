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
            ModelAndView modelAndView = new ModelAndView();
            String viewName = UrlFormatter.format(req.getRequestUri());
            modelAndView.setViewName(viewName);
            return modelAndView;
        });

        runner.put("GET/user/create", (req, res) -> {
            ModelAndView modelAndView = new ModelAndView();
            loginService.join(req.getParams());
            String viewName = UrlFormatter.format(WebConst.HOME);
            modelAndView.setViewName(viewName);
            return modelAndView;
        });
    }


}
