package com.kakao.example.controller;

import com.kakao.example.application.service.UserService;
import com.kakao.example.application.service.UserServiceImpl;
import com.kakao.example.model.domain.User;
import framework.controller.Controller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import framework.util.annotation.RequestMapping;
import framework.webserver.HttpRequestHandler;
import framework.webserver.HttpResponseHandler;

@RequestMapping("/user")
public class UserController implements Controller {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);
    private static Controller instance;

    private final UserService userService = UserServiceImpl.getInstance();

    public static Controller getInstance() {
        instance = new UserController();
        return instance;
    }

    private UserController() {};

    @RequestMapping(value = "/create", requestMethod = "GET")
    public String registerByGet(HttpRequestHandler request) {
        LOGGER.debug("Register User by GET method");

        userService.addUser(User.builder()
                .userId(request.getAttribute("userId"))
                .password(request.getAttribute("password"))
                .name(request.getAttribute("name"))
                .email(request.getAttribute("email"))
                .build());

        return "redirect:/index";
    }

    @RequestMapping(value = "/create", requestMethod = "POST")
    public String registerByPost(HttpRequestHandler request) {
        LOGGER.debug("Register User by POST method");

        userService.addUser(User.builder()
                .userId(request.getAttribute("userId"))
                .password(request.getAttribute("password"))
                .name(request.getAttribute("name"))
                .email(request.getAttribute("email"))
                .build());

        return "redirect:/index";
    }

    @RequestMapping(value = "/login", requestMethod = "POST")
    public String login(HttpRequestHandler request, HttpResponseHandler response) {
        LOGGER.debug("Login User");
        String userId = request.getAttribute("userId");
        String password = request.getAttribute("password");

        try {
            userService.findUserByLoginInfo(userId, password);
        } catch (Exception e) {
            response.setCookie("logined", "false", "/");
            return "redirect:/user/login_failed";
        }

        response.setCookie("logined", "true", "/");
        return "redirect:/index";
    }
}
