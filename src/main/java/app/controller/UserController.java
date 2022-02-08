package app.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;

import app.model.User;
import app.service.UserService;
import webserver.annotation.RequestMapping;
import webserver.http.HttpRequest;
import webserver.http.HttpRequestMethod;
import webserver.http.HttpResponse;
import webserver.util.Constant;

public class UserController {
    private static UserController instance = new UserController();

    private UserController() {
    }

    public static UserController getInstance() {
        return instance;
    }

    @RequestMapping(value = "/user/create", method = HttpRequestMethod.POST)
    public void userCreate(HttpRequest request, HttpResponse response) throws IOException {
        Map<String, String> body = request.getBodyParams();

        UserService.getInstance().saveUser(
                new User(body.get("userId"),
                         body.get("password"),
                         body.get("name"),
                         body.get("email"))
        );

        response.sendRedirect(Constant.INDEX_PATH);
    }

    @RequestMapping(value = "/user/login", method = HttpRequestMethod.POST)
    public void login(HttpRequest request, HttpResponse response) throws IOException {
        Map<String, String> body = request.getBodyParams();
        User user = UserService.getInstance().getUser(body.get("userId"));

        if (ObjectUtils.isNotEmpty(user) && StringUtils.equals(user.getPassword(), body.get("password"))) {
            response.sendRedirectWithCookie(Constant.INDEX_PATH, Constant.LOGIN_CACHE);
        } else {
            response.sendRedirect(Constant.LOGIN_FAILED_PATH);
        }
    }

    @RequestMapping(value = "/user/logout", method = HttpRequestMethod.GET)
    public void logout(HttpRequest request, HttpResponse response) throws IOException {
        response.sendRedirectWithCookie(Constant.INDEX_PATH, Constant.LOGOUT_CACHE);
    }


    @RequestMapping(value = "/user/list", method = HttpRequestMethod.GET)
    public void userList(HttpRequest request, HttpResponse response) throws IOException {
        String cookie = request.getCookie();

        if (StringUtils.contains(cookie, Constant.LOGIN_CACHE)) {
            Map<String, String> map = new HashMap<>();
            map.put("\\{\\{userList\\}\\}", UserService.getInstance().getUserListHtml());

            response.sendDynamicHtml(Constant.USER_LIST_PATH, map);
        } else {
            response.sendRedirect(Constant.LOGIN_PATH);
        }
    }
}
