package application.controller;

import application.domain.UserRepository;
import application.domain.User;
import was.domain.controller.Controller;
import was.domain.controller.StaticResourceController;
import was.domain.http.Cookie;
import was.domain.http.HttpResponse;
import was.meta.HttpHeaders;
import was.meta.HttpStatus;
import was.meta.UrlPath;

import java.util.Objects;
import java.util.Optional;

public class UserController {

    private final UserRepository userRepository = new UserRepository();

    public Controller signUp = (req, res) -> {
        final String id = req.getRequestParam("id");
        final String pwd = req.getRequestParam("password");

        final User user = new User(id, pwd);

        userRepository.save(user);

        res.setStatus(HttpStatus.FOUND);
        res.addHeader(HttpHeaders.LOCATION, UrlPath.LOGIN_FORM);
    };

    public Controller loginForm = (req, res) -> {
        final Cookie cookie = req.getCookie();
        final String loginAttribute = cookie.get("isLogin");

        final boolean isLogin = loginAttribute != null && Objects.equals(loginAttribute, "True");

        if (isLogin) {
            res.setStatus(HttpStatus.FOUND);
            res.addHeader(HttpHeaders.LOCATION, UrlPath.HOME);
            return;
        }

        res.setViewPath(req.getPath());
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
        res.addHeader(HttpHeaders.LOCATION, UrlPath.HOME);
        res.addCookie(cookie);
    }

    private void setFailResponse(HttpResponse res) {
        final Cookie cookie = new Cookie();
        cookie.put("isLogin", "False");
        cookie.put("path", "/");

        res.setStatus(HttpStatus.FOUND);
        res.addHeader(HttpHeaders.LOCATION, UrlPath.LOGIN_FAIL);
        res.addCookie(cookie);
    }

    private boolean validate(String pwd, User user) {
        return user.getPassword().equals(pwd);
    }
}
