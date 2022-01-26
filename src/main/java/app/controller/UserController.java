package app.controller;

import annotation.Controller;
import annotation.RequestMapping;
import app.model.User;
import app.service.UserService;
import http.request.HttpMethod;
import http.request.HttpRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
public class UserController {
    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    private final UserService userService;

    public UserController() {
        this.userService = UserService.getInstance();
    }

    @RequestMapping(method = HttpMethod.GET, uri = "/user/create")
    public String signUpByGet(HttpRequest httpRequest) {
        User user = new User(
                httpRequest.getQuery("userId"),
                httpRequest.getQuery("password"),
                httpRequest.getQuery("name"),
                httpRequest.getQuery("email"));

        log.info(user.toString());

        userService.signupUser(user);
        return "/index.html";
    }
}
