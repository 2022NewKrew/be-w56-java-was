package webserver.controller;

import webserver.http.HttpConst;
import webserver.http.HttpRequest;
import webserver.http.HttpResponse;
import webserver.http.HttpStatus;
import model.User;
import webserver.service.UserService;

public class RegisterController implements HttpController{
    private static final UserService userService = new UserService();

    @Override
    public HttpResponse process(HttpRequest request){
        User user = new User(
                request.getBody("userId"),
                request.getBody("password"),
                request.getBody("name"),
                request.getBody("email")
        );

        userService.join(user);

        HttpResponse response = new HttpResponse(HttpStatus.FOUND);
        response.setHeader("Location", "/");

        return response;
    }
}
