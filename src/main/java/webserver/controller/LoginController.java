package webserver.controller;

import exception.UnAuthorizedException;
import model.User;
import webserver.http.HttpRequest;
import webserver.http.HttpResponse;
import webserver.http.HttpStatus;
import webserver.service.UserService;

public class LoginController implements HttpController{
    private final UserService userService = new UserService();
    @Override
    public HttpResponse process(HttpRequest request) {
        String userId = request.getBody("userId");
        String password = request.getBody("password");

        User user = userService.findUser(userId);

        HttpResponse response = new HttpResponse(HttpStatus.FOUND);
        if(user == null || !user.getPassword().equals(password)){
            response.setHeader("Location", "/user/login_failed.html");
            response.setCookie("logined", "false");
            return response;
        }

        response.setHeader("Location", "/");
        response.setCookie("logined", "true");
        response.setCookie("Path", "/");
        return response;
    }
}
