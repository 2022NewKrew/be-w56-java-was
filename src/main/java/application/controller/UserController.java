package application.controller;

import application.domain.User;
import application.domain.UserService;
import di.annotation.Bean;
import was.http.annotation.Controller;
import was.http.annotation.GetMapping;
import was.http.annotation.PostMapping;
import was.http.domain.request.Cookie;
import was.http.domain.request.HttpRequest;
import was.http.domain.response.HttpResponse;
import was.http.domain.service.view.ModelAndView;
import was.http.domain.service.view.ResponseEntity;
import was.http.domain.service.view.ViewType;
import was.http.meta.HttpHeaders;
import was.http.meta.HttpStatus;

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
    public User signUp(HttpRequest req) {
        final String id = req.getRequestParam("id");
        final String pwd = req.getRequestParam("password");

        final User user = new User(id, pwd);

        userService.create(user);

        return user;
    }

    @GetMapping(path = "/login")
    public ModelAndView loginForm(HttpRequest req) {
        final Cookie cookie = req.getCookie();
        final String loginAttribute = cookie.get("isLogin");

        final boolean isLogin = loginAttribute != null && Objects.equals(loginAttribute, "True");

        if (isLogin) {
            return new ModelAndView(ViewType.REDIRECT, "/");
        }

        return new ModelAndView(ViewType.STATIC_RESOURCE, "/templates/login.html");
    }

    @GetMapping(path = "/users?type=json")
    public ResponseEntity<List<User>> getAllJsonType() {
        final List<User> users = userService.getAll();

        return ResponseEntity.<List<User>>builder()
                .status(HttpStatus.OK)
                .body(users)
                .build();
    }

    @GetMapping(path = "/users")
    public ModelAndView getAll() {
        final List<User> users = userService.getAll();

        for (User user : users) {
            users.get(0).addFriend(user);
        }

        return new ModelAndView(ViewType.STATIC_RESOURCE, "/templates/home.html")
                .addModel("users", users);
    }

    @PostMapping(path = "/users/login")
    public void login(HttpRequest req, HttpResponse res) {
        final String id = req.getRequestParam("id");
        final String pwd = req.getRequestParam("password");

        if (!userService.login(id, pwd)) {
            final Cookie cookie = new Cookie();
            cookie.put("isLogin", "False");
            cookie.put("path", "/");

            res.setStatus(HttpStatus.FOUND);
            res.addHeader(HttpHeaders.LOCATION, "/templates/login_fail.html");
            res.addCookie(cookie);
            return;
        }

        final Cookie cookie = new Cookie();
        cookie.put("isLogin", "True");
        cookie.put("path", "/");

        res.setStatus(HttpStatus.FOUND);
        res.addHeader(HttpHeaders.LOCATION, "/");
        res.addCookie(cookie);
    }
}
