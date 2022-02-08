package controller;

import controller.annotation.RequestMapping;
import db.DataBase;
import http.request.HttpRequest;
import http.response.HttpResponse;
import http.response.Model;
import http.response.ModelAndView;
import lombok.extern.slf4j.Slf4j;
import model.User;
import repository.UserRepository;

import java.util.List;

@Slf4j
public class RequestUrlController {

    private final UserRepository userRepository = new UserRepository();

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
        userRepository.save(user);

        log.info("user created = {}", user);
        return new ModelAndView("redirect:/");
    }

    @RequestMapping(value = "/login", method = "POST")
    public ModelAndView login(HttpRequest request, HttpResponse response) {
        String userId = request.getParam("userId");
        String password = request.getParam("password");
        User user = userRepository.findByUserId(userId).orElse(null);

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
        List<User> users = userRepository.findAll();
        Model model = new Model();
        model.addAttribute("users", users);
        return new ModelAndView("/user/list", model);
    }
}
