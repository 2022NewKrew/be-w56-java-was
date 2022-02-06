package app.controller;

import annotation.Controller;
import annotation.RequestMapping;
import app.dto.UserLoginInfo;
import app.model.User;
import app.service.UserService;
import handler.HandlerResult;
import http.request.HttpMethod;
import http.request.HttpRequest;
import http.response.Cookie;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
public class UserController {
    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    private final UserService userService;

    public UserController() {
        this.userService = UserService.getInstance();
    }

    @RequestMapping(method = HttpMethod.POST, uri = "/users")
    public HandlerResult signupByPost(HttpRequest httpRequest) {
        User user = new User(
                httpRequest.getBody("userId"),
                httpRequest.getBody("password"),
                httpRequest.getBody("name"),
                httpRequest.getBody("email"));

        log.debug(user.toString());
        userService.signupUser(user);

        return HandlerResult.Builder.ofRedirect()
                .uri("/")
                .build();
    }

    @RequestMapping(method = HttpMethod.POST, uri="/users/login")
    public HandlerResult login(HttpRequest httpRequest) {
        UserLoginInfo userLoginInfo = new UserLoginInfo(
                httpRequest.getBody("userId"),
                httpRequest.getBody("password"));

        log.debug(userLoginInfo.toString());
        if (userService.loginUser(userLoginInfo) == null) {
            Cookie cookie = Cookie.Builder
                    .of("logined", "false")
                    .path("/")
                    .build();
            return HandlerResult.Builder.ofRedirect()
                    .uri("/user/login_failed.html")
                    .addCookie(cookie)
                    .build();
        }

        Cookie cookie = Cookie.Builder
                .of("logined", "true")
                .path("/")
                .build();
        return HandlerResult.Builder.ofRedirect()
                .uri("/")
                .addCookie(cookie)
                .build();
    }
}
