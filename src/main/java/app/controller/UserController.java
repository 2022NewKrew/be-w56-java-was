package app.controller;

import java.util.Map;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;

import app.db.DataBase;
import app.model.User;
import webserver.annotation.RequestMapping;
import webserver.http.HttpRequest;
import webserver.http.HttpRequestMethod;
import webserver.http.HttpResponse;

public class UserController {
    private static UserController instance = new UserController();

    private UserController() {
    }

    public static UserController getInstance() {
        return instance;
    }

    @RequestMapping(value = "/user/create", method = HttpRequestMethod.POST)
    public void create(HttpRequest request, HttpResponse response) {
        Map<String, String> body = request.getBodyParams();

        DataBase.addUser(
                new User(body.get("userId"),
                         body.get("password"),
                         body.get("name"),
                         body.get("email"))
        );

        response.sendRedirect("/index.html");
    }

    @RequestMapping(value = "/user/login", method = HttpRequestMethod.POST)
    public void login(HttpRequest request, HttpResponse response) {
        Map<String, String> body = request.getBodyParams();
        User user = DataBase.findUserById(body.get("userId"));

        if (ObjectUtils.isNotEmpty(user) && StringUtils.equals(user.getPassword(), body.get("password"))) {
            System.out.println("UserController.logineddddddd");
            response.sendRedirectWithCookie("/index.html");
        } else {
            System.out.println("UserController.login failedddddd");
            response.sendRedirect("/user/login_failed.html");
        }

    }
}
