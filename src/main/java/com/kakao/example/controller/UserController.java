package com.kakao.example.controller;

import com.kakao.example.application.service.UserService;
import com.kakao.example.model.domain.User;
import com.kakao.example.util.exception.UserNotFoundException;
import framework.util.HttpSession;
import framework.util.annotation.Autowired;
import framework.util.annotation.Component;
import framework.util.annotation.RequestMapping;
import framework.view.ModelView;
import framework.webserver.HttpRequestHandler;
import framework.webserver.HttpResponseHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static framework.util.annotation.Component.ComponentType.CONTROLLER;

@Component(type = CONTROLLER)
@RequestMapping("/user")
public class UserController {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;

        userService.addUser(User.builder()
                .userId("admin")
                .password("admin")
                .name("관리자")
                .email("admin@kakao.com")
                .build());
    }

    @RequestMapping(value = "/create", requestMethod = "GET")
    public String userRegisterByGet(HttpRequestHandler request) {
        LOGGER.debug("Register User by GET method");

        userService.addUser(User.builder()
                .userId(request.getAttribute("userId"))
                .password(request.getAttribute("password"))
                .name(request.getAttribute("name"))
                .email(request.getAttribute("email"))
                .build());

        return "redirect:/";
    }

    @RequestMapping(value = "/create", requestMethod = "POST")
    public String userRegisterByPost(HttpRequestHandler request) {
        LOGGER.debug("Register User by POST method");

        userService.addUser(User.builder()
                .userId(request.getAttribute("userId"))
                .password(request.getAttribute("password"))
                .name(request.getAttribute("name"))
                .email(request.getAttribute("email"))
                .build());

        return "redirect:/";
    }

    @RequestMapping(value = "/login", requestMethod = "POST")
    public String userLogin(HttpRequestHandler request, HttpResponseHandler response) {
        LOGGER.debug("Login User");
        String userId = request.getAttribute("userId");
        String password = request.getAttribute("password");

        HttpSession session = request.getSession();

        try {
            userService.findUserByLoginInfo(userId, password);
        } catch (UserNotFoundException e) {
            return "redirect:/user/login_failed";
        }

        session.setAttribute("USER_ID", userId);
        return "redirect:/";
    }

    @RequestMapping(value = "/logout", requestMethod = "GET")
    public String userLogout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }

    @RequestMapping(value = "/list", requestMethod = "GET")
    public String userList(HttpSession session, ModelView modelView) {
        LOGGER.debug("Get User List");

        if (!session.contains("USER_ID")) {
            return "user/login";
        }

        modelView.setAttribute("users", userService.findAll());
        return "user/list";
    }
}
