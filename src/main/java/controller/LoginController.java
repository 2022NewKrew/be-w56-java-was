package controller;

import jdbc.DataBase;
import model.User;
import mvcframework.RequestMethod;
import mvcframework.annotation.Controller;
import mvcframework.annotation.RequestMapping;
import webserver.HttpRequest;
import webserver.HttpResponse;
import webserver.HttpSession;

@Controller
public class LoginController {

    @RequestMapping(value = "/user/login", method = RequestMethod.POST)
    public void login(HttpRequest request, HttpResponse response) {
        User user = DataBase.findUserById(request.getParam("userId"));
        if (user != null) {
            if (user.login(request.getParam("password"))) {
                HttpSession session = request.getSession();
                session.setAttribute("user", user);
                response.sendRedirect("/index.html");
            } else {
                response.sendRedirect("/user/login_failed.html");
            }
        } else {
            response.sendRedirect("/user/login_failed.html");
        }
    }
}
