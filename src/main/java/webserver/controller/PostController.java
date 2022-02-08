package webserver.controller;

import network.HttpRequest;
import network.HttpResponse;
import network.Status;
import webserver.service.UserService;

import java.io.OutputStream;

public class PostController {

    private UserService userService;
    private OutputStream outputStream;

    public PostController(HttpRequest httpRequest, OutputStream outputStream) {
        this.outputStream = outputStream;
        this.userService = new UserService(httpRequest);
    }

    public void handlePost(String path) {
        switch (path) {
            case "/user/create":
                userService.signUp();
                HttpResponse.redirect("/index.html", outputStream);
                break;
            case "/user/login":
                Status status = userService.logIn();
                if (status == Status.OK) {
                    HttpResponse.redirect("/index.html", outputStream);
                    break;
                }
                HttpResponse.redirect("/user/login_failed.html", outputStream);
                break;
        }
    }
}
