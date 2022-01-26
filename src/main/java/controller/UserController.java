package controller;

import domain.User;
import domain.UserRepository;
import was.domain.controller.Controller;
import was.domain.controller.StaticResourceController;
import was.domain.http.Cookie;
import was.domain.http.HttpResponse;
import was.meta.HttpHeaders;
import was.meta.HttpStatus;
import was.meta.UrlPath;

import java.util.Optional;

public class UserController {

    private final UserRepository userRepository = new UserRepository();

    public Controller signUp = (req, res) -> {
        final String id = req.getRequestParam("id");
        final String pwd = req.getRequestParam("password");

        final User user = new User(id, pwd);

        userRepository.save(user);

        res.setStatus(HttpStatus.FOUND);
        res.addHeader(HttpHeaders.LOCATION, UrlPath.LOGIN_FORM.getPath());
    };

    public Controller loginForm = (req, res) -> {
        final Cookie cookie = req.getCookie();
        final String isLoginStr = cookie.get("isLogin");

        if (isLoginStr == null || !isLoginStr.equals("True")) {
            final StaticResourceController staticResourceController = StaticResourceController.getInstance();
            staticResourceController.handle(req, res);
            return;
        }

        res.setStatus(HttpStatus.FOUND);
        res.addHeader(HttpHeaders.LOCATION, UrlPath.HOME.getPath());
    };

    public Controller login = (req, res) -> {
        final String id = req.getRequestParam("id");
        final String pwd = req.getRequestParam("password");

        final Optional<User> user = userRepository.findById(id);

        if (user.isEmpty() || !validate(pwd, user.get())) {
            setFailResponse(res);
            return;
        }

        setSuccessResponse(res);
    };

    private void setSuccessResponse(HttpResponse res) {
        final Cookie cookie = new Cookie();
        cookie.put("isLogin", "True");
        cookie.put("path", "/");

        res.setStatus(HttpStatus.FOUND);
        res.addHeader(HttpHeaders.LOCATION, UrlPath.HOME.getPath());
        res.addCookie(cookie);
    }

    private void setFailResponse(HttpResponse res) {
        final Cookie cookie = new Cookie();
        cookie.put("isLogin", "False");
        cookie.put("path", "/");

        res.setStatus(HttpStatus.FOUND);
        res.addHeader(HttpHeaders.LOCATION, UrlPath.LOGIN_FAIL.getPath());
        res.addCookie(cookie);
    }

    private boolean validate(String pwd, User user) {
        return user.getPassword().equals(pwd);
    }
}
