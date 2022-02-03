package webserver.controller;

import service.UserService;
import util.annotation.RequestMapping;
import webserver.Request;
import webserver.Response;

public class LoginController {
    private static final LoginController instance = new LoginController();
    private static final UserService userService = UserService.getInstance();

    private LoginController() {}

    public static LoginController getInstance() {
        return instance;
    }

    @RequestMapping(value="/login", method="POST")
    public String login(Request request, Response response) {
        if(userService.login(request.getParameter("stringId"), request.getParameter("password"))){
            response.setCookie("logined", "true; Path=/");
            return "redirect:/index.html";
        }
        response.setCookie("logined", "false; Path=/");
        return "redirect:/user/login_failed.html";
    }
}
