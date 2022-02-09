package webserver.controller;

import network.HttpRequest;
import network.HttpResponse;
import webserver.service.UserService;

import java.io.OutputStream;

public class PostController {

    private UserService userService;
    private HttpResponse httpResponse;

    public PostController(HttpRequest httpRequest, OutputStream outputStream) {
        this.httpResponse = new HttpResponse(outputStream);
        this.userService = new UserService(httpRequest);
    }

    public void handlePost(String path) {
        httpResponse.setPath(path);

        switch (path) {
            case "/user/create":
                userService.signUp();
                httpResponse.redirect("/index.html");
                break;
            case "/user/login":
                Boolean cookie = userService.logIn();
                if (cookie) {
                    httpResponse.setCookie("logined", "true");
                    httpResponse.redirect("/index.html");
                    break;
                }
                httpResponse.setCookie("logined", "false");
                httpResponse.redirect("/user/login_failed.html");
                break;
        }
    }
}
