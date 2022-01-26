package webserver.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.http.HttpRequest;
import webserver.http.HttpResponse;
import webserver.http.HttpStatus;
import model.User;
import webserver.service.UserService;

public class RegisterController implements HttpController{
    private static final Logger log = LoggerFactory.getLogger(RegisterController.class);
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
        log.info("Create User - {}", user);

        HttpResponse response = new HttpResponse(HttpStatus.FOUND);
        response.setHeader("Location", "/");

        return response;
    }
}
