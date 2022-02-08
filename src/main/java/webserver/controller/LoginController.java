package webserver.controller;

import service.UserService;
import util.annotation.RequestMapping;
import webserver.view.ModelAndView;
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
    public ModelAndView login(Request request, Response response) {
        ModelAndView mv = new ModelAndView();
        if(userService.login(request.getParameter("stringId"), request.getParameter("password"))){
            response.setCookie("logined", "true; Path=/");
            mv.setViewName("redirect:/index.html");
            return mv;
        }
        response.setCookie("logined", "false; Path=/");
        mv.setViewName("redirect:/user/login_failed.html");
        return mv;
    }
}
