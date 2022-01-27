package controller;

import annotation.Controller;
import annotation.PostMapping;
import db.DataBase;
import exception.InvalidRequestException;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.request.Request;
import webserver.response.Response;
import webserver.response.ResponseFactory;

@Controller
public final class UserController {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    private static final String HOME_URL = "/index.html";

    @PostMapping("/user/create")
    public Response userAdd(Request request) throws InvalidRequestException {
        log.debug("userController.userAdd called");
        if (DataBase.findUserById(request.getQuery("userId")) != null) {
            throw new InvalidRequestException("UserId 중복");
        }
        DataBase.addUser(User.builder()
            .userId(request.getQuery("userId"))
            .password(request.getQuery("password"))
            .name(request.getQuery("name"))
            .email(request.getQuery("email"))
            .build());
        return ResponseFactory.redirect(HOME_URL);
    }

    @PostMapping("/user/login")
    public Response userLogin(Request request) {
        String userId = request.getQuery("userId");
        String password = request.getQuery("password");
        log.debug("user login 시도: {}, {}", userId, password);

        if (DataBase.findUserById(userId) == null ||
            !DataBase.findUserById(userId).getPassword().equals(password)) {
            return ResponseFactory.redirect("/user/login_failed.html")
                .setCookie("logined=false");
        }
        return ResponseFactory.redirect(HOME_URL)
            .setCookie("logined=true&userId=" + request.getQuery("userId"), "Path=/");
    }
}
