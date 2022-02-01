package com.kakao.example.controller;

import com.kakao.example.application.service.UserService;
import com.kakao.example.application.service.UserServiceImpl;
import com.kakao.example.model.domain.User;
import com.kakao.example.util.exception.UserNotFoundException;
import framework.controller.Controller;
import framework.util.annotation.Autowired;
import framework.util.annotation.Component;
import framework.util.annotation.Primary;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import framework.util.annotation.RequestMapping;
import framework.webserver.HttpRequestHandler;
import framework.webserver.HttpResponseHandler;

import static framework.util.annotation.Component.ComponentType.CONTROLLER;

@Component(type = CONTROLLER)
@RequestMapping("/user")
public class UserController implements Controller {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    private UserService userService;

    public UserController() {}

    @Autowired
    @Primary
    public UserController(UserService userService) {
        this.userService = userService;
    }

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
        } catch (UserNotFoundException e) {
            response.setCookie("logined", "false", "/");
            return "redirect:/user/login_failed";
        }

        response.setCookie("logined", "true", "/");
        return "redirect:/index";
    }
}
