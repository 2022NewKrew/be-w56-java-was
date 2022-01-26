package controller;

import annotation.Controller;
import annotation.PostMapping;
import db.DataBase;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.request.Request;
import webserver.response.Response;

@Controller
public final class UserController {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    @PostMapping("/user/create")
    public void userAdd(Request request, Response response) {
        log.debug("userController.userAdd called");
        DataBase.addUser(User.builder()
            .userId(request.getQuery("userId"))
            .password(request.getQuery("password"))
            .name(request.getQuery("name"))
            .email(request.getQuery("email"))
            .build());
        response.redirectTo("/index.html");
    }
}
