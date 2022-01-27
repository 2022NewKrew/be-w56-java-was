package webserver.controller;

import annotation.RequestMapping;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.http.HttpMethod;
import webserver.http.HttpRequest;
import webserver.http.HttpResponse;
import webserver.http.HttpStatus;
import webserver.service.UserService;

public class UserController {
    private static UserController instance = null;
    private static final UserService userService = new UserService();
    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    private UserController(){}

    public static UserController getInstance() {
        if(instance == null){
            instance = new UserController();
        }

        return instance;
    }

    @RequestMapping(method = HttpMethod.POST, url = "/user/create")
    public HttpResponse register(HttpRequest request){
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

    @RequestMapping(method = HttpMethod.POST, url = "/user/login")
    public HttpResponse login(HttpRequest request){
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
