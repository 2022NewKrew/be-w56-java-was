package webserver.controller;

import domain.User;
import network.HttpRequest;
import network.HttpResponse;
import webserver.service.UserService;

import java.io.OutputStream;

public class PostController {

    private UserService userService;
    private OutputStream outputStream;

    public PostController(HttpRequest httpRequest, OutputStream outputStream){
        this.outputStream = outputStream;
        this.userService = new UserService(httpRequest);
    }

    public void handlePost(String path) {
        switch (path){
            case "/user/create":
                userService.signUp();
                HttpResponse.redirect("/index.html", outputStream);
                break;
        }
    }
}
