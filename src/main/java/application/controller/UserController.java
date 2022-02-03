package application.controller;

import application.domain.User;
import application.domain.UserService;
import di.annotation.Bean;
import was.domain.controller.annotation.Controller;
import was.domain.controller.annotation.GetMapping;
import was.domain.controller.annotation.PostMapping;
import was.domain.http.Cookie;
import was.domain.http.HttpRequest;
import was.domain.http.HttpResponse;
import was.meta.HttpHeaders;
import was.meta.HttpStatus;
import application.meta.UrlPath;
import was.meta.MediaTypes;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;


@Controller
@Bean
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping(path = "/signUp")
    public void signUp(HttpRequest req, HttpResponse res) {
        final String id = req.getRequestParam("id");
        final String pwd = req.getRequestParam("password");

        final User user = new User(id, pwd);

        userService.create(user);

        res.setStatus(HttpStatus.FOUND);
        res.addHeader(HttpHeaders.LOCATION, UrlPath.LOGIN_FORM);
    }

    @GetMapping(path = "/login")
    public void loginForm(HttpRequest req, HttpResponse res) {
        final Cookie cookie = req.getCookie();
        final String loginAttribute = cookie.get("isLogin");

        final boolean isLogin = loginAttribute != null && Objects.equals(loginAttribute, "True");

        if (isLogin) {
            res.setStatus(HttpStatus.FOUND);
            res.addHeader(HttpHeaders.LOCATION, UrlPath.HOME);
            return;
        }

        res.setViewPath(UrlPath.LOGIN_FORM);
    }

    @GetMapping(path = "/users")
    public void getAll(HttpRequest req, HttpResponse res) {
        final StringBuilder sb = new StringBuilder();
        final List<User> users = userService.getAll();

        sb.append("<ul>");
        users.forEach(user -> sb.append("<li>").append(user.getName()).append("</li>"));
        sb.append("</ul>");

        final String body = sb.toString();

        res.setStatus(HttpStatus.OK);
        res.addHeader(HttpHeaders.CONTENT_TYPE, MediaTypes.TEXT_HTML.getValue());
        res.setBody(body.getBytes(StandardCharsets.UTF_8));
    }

    @PostMapping(path = "/login")
    public void login(HttpRequest req, HttpResponse res) {
        final String id = req.getRequestParam("id");
        final String pwd = req.getRequestParam("password");

        if (!userService.login(id, pwd)) {
            final Cookie cookie = new Cookie();
            cookie.put("isLogin", "False");
            cookie.put("path", "/");

            res.setStatus(HttpStatus.FOUND);
            res.addHeader(HttpHeaders.LOCATION, UrlPath.LOGIN_FAIL);
            res.addCookie(cookie);
            return;
        }

        final Cookie cookie = new Cookie();
        cookie.put("isLogin", "True");
        cookie.put("path", "/");

        res.setStatus(HttpStatus.FOUND);
        res.addHeader(HttpHeaders.LOCATION, UrlPath.HOME);
        res.addCookie(cookie);
    }
}
