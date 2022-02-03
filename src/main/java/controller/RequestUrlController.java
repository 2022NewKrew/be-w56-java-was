package controller;

import controller.annotation.RequestMapping;
import db.DataBase;
import http.request.HttpRequest;
import http.response.HttpResponse;
import http.response.Model;
import http.response.ModelAndView;
import lombok.extern.slf4j.Slf4j;
import model.User;

@Slf4j
public class RequestUrlController {

    @RequestMapping("/")
    public ModelAndView index(HttpRequest request, HttpResponse response) {
        String logined = request.getCookie("logined");
        if (logined == null) {
            return new ModelAndView("/index");
        }
        return new ModelAndView("redirect:/users");
    }

    @RequestMapping(value = "/create", method = "POST")
    public ModelAndView createUser(HttpRequest request, HttpResponse response) {
        String userId = request.getParam("userId");
        String password = request.getParam("password");
        String name = request.getParam("name");
        String email = request.getParam("email");
        User user = new User(userId, password, name, email);
        DataBase.addUser(user);

        log.info("user created = {}", user);
        return new ModelAndView("redirect:/");
    }

    @RequestMapping(value = "/login", method = "POST")
    public ModelAndView login(HttpRequest request, HttpResponse response) {
        String userId = request.getParam("userId");
        String password = request.getParam("password");
        User user = DataBase.findUserById(userId);
        if (user == null || !user.getPassword().equals(password)) {
            response.addCookie("logined", "false");
            return new ModelAndView("redirect:/login-failed");
        }
        response.addCookie("logined", "true");
        return new ModelAndView("redirect:/");
    }

    @RequestMapping("/login-failed")
    public ModelAndView loginFailed(HttpRequest request, HttpResponse response) {
        return new ModelAndView("/user/login_failed");
    }

    @RequestMapping("/users")
    public ModelAndView showUserList(HttpRequest request, HttpResponse response) {
        Model model = new Model();
        model.addAttribute("userId", "carrot");
        model.addAttribute("name", "당근");
        model.addAttribute("email", "kain6245@gmail.com");
        return new ModelAndView("/user/list", model);
    }
}
