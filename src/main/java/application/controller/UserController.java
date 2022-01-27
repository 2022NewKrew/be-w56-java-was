package application.controller;

import application.model.User;
import application.model.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import was.controller.Controller;
import was.http.Cookie;
import was.meta.HttpHeader;
import was.meta.HttpStatus;
import was.meta.UrlPath;

public class UserController {
    private final Logger log = LoggerFactory.getLogger(UserController.class);

    public Controller signup = (request, response) -> {
        String userId = request.getRequestParam("userId");
        String password = request.getRequestParam("password");
        String name = request.getRequestParam("name");
        String email = request.getRequestParam("email");

        User user = new User(userId, password, name, email);
        UserRepository.addUser(user);

        log.info("[SIGNUP] " + user.toString());

        response.setStatus(HttpStatus.FOUND);
        response.addHeader(HttpHeader.LOCATION, UrlPath.INDEX.getPath());
    };

    public Controller login = (request, response) -> {
        String userId = request.getRequestParam("userId");
        String password = request.getRequestParam("password");

        User user = UserRepository.findUserById(userId);

        if (user.validatePassword(password)) {
            log.info("[LOGIN][SUCCESS] " + user.toString());

            response.setStatus(HttpStatus.FOUND);
            response.addHeader(HttpHeader.LOCATION, UrlPath.INDEX.getPath());
            response.addCookie(Cookie.ATTRIBUTE_LOGINED, "true");
            response.addCookie(Cookie.ATTRIBUTE_PATH, "/");

            return;
        }

        log.info("[LOGIN][FAIL] " + user.toString());

        response.setStatus(HttpStatus.FOUND);
        response.addHeader(HttpHeader.LOCATION, UrlPath.LOGIN_FAILED.getPath());
        response.addCookie(Cookie.ATTRIBUTE_LOGINED, "false");
        response.addCookie(Cookie.ATTRIBUTE_PATH, "/");
    };
}
