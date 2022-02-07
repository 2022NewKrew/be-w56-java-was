package webserver.controller;

import db.DataBase;
import http.HttpHeader;
import http.HttpStatusCode;
import http.request.HttpRequest;
import http.response.HttpResponse;
import http.util.HttpRequestUtils;
import http.util.IOUtils;
import model.User;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Map;
import java.util.Objects;

public class UserController implements Controller {
    private static final Controller instance = new UserController();

    private UserController() {
    }

    public static Controller getInstance() {
        return instance;
    }

    @Override
    public String handleGet(HttpRequest request, HttpResponse response) {
        return null;
    }

    @Override
    public String handlePost(HttpRequest request, HttpResponse response) {
        if (request.getStartLine().getTargetUri().equals("/user/create")) {
            return postCreateUser(request, response);
        }
        if (request.getStartLine().getTargetUri().equals("/user/login")) {
            return postLoginUser(request, response);
        }
        return null;
    }

    private String postCreateUser(HttpRequest request, HttpResponse response) {
        String bodyString = request.getHttpBody().getBody();
        Map<String, String> bodyParams = HttpRequestUtils.parseQueryString(bodyString);

        User user = new User(
                bodyParams.get("userId"),
                bodyParams.get("password"),
                bodyParams.get("name"),
                bodyParams.get("email")
        );

        DataBase.addUser(user);

        return "redirect:/";
    }

    private String postLoginUser(HttpRequest request, HttpResponse response) {
        String bodyString = request.getHttpBody().getBody();
        Map<String, String> bodyParams = HttpRequestUtils.parseQueryString(bodyString);

        User user = DataBase.findUserById(bodyParams.get("userId"));
        if (Objects.isNull(user) || !user.isCorrectPassword(bodyParams.get("password"))) {
            response.getHeader().addHeader("Set-Cookie: logined=false; Path=/");
            return "redirect:/user/login_failed.html";
        }

        response.getHeader().addHeader("Set-Cookie: logined=true; Path=/");
        return "redirect:/";
    }
}
