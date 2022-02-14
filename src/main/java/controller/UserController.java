package controller;

import service.UserService;
import webserver.http.request.HttpRequest;
import webserver.http.response.HttpResponse;

import java.io.IOException;

public class UserController implements Controller {
    UserService userService;

    public UserController() {
        this.userService = UserService.getInstance();
    }

    @Override
    public HttpResponse handle(HttpRequest httpRequest) throws IOException {
        String url = httpRequest.getUrlPath();
        if (url.equals("/user/create")) {
            return HttpResponse.of(create(httpRequest), 302);
        }
        return null;
    }

    private String create(HttpRequest httpRequest) {
        userService.createUser(httpRequest.getInfoMap());
        return "/index.html";
    }
}
