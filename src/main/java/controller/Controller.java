package controller;

import annotation.GetMapping;
import annotation.PostMapping;
import annotation.RequestBody;
import annotation.RequestParam;
import controller.dto.EnrollUserRequest;
import controller.dto.LoginUserRequest;
import service.UserService;
import service.dto.EnrollUserCommand;
import service.dto.LoginUserCommand;
import service.dto.LoginUserResult;
import webserver.header.Cookie;
import webserver.response.Response;


public class Controller {

    @GetMapping(path = "/")
    public Response getIndex() {
        return new Response("/index.html");
    }

    @GetMapping(path = "/user/create")
    public Response signUp(
            @RequestParam(name = "userId") String userId,
            @RequestParam(name = "password") String password,
            @RequestParam(name = "name") String name,
            @RequestParam(name = "email") String email) {

        UserService.enroll(new EnrollUserCommand(userId, password, name, email));

        return new Response("redirect:/");
    }

    @PostMapping(path = "/user/create")
    public Response signUp(@RequestBody(names = {"userId", "password", "name", "email"}) EnrollUserRequest enrollUserRequest) {

        UserService.enroll(
                new EnrollUserCommand(
                        enrollUserRequest.getUserId(),
                        enrollUserRequest.getPassword(),
                        enrollUserRequest.getName(),
                        enrollUserRequest.getEmail()
                ));

        return new Response("redirect:/");
    }

    @PostMapping(path = "/user/login")
    public Response login(@RequestBody(names = {"userId", "password"}) LoginUserRequest loginUserRequest) {

        LoginUserResult result = UserService.getUserInfo(
                new LoginUserCommand(
                        loginUserRequest.getUserId(),
                        loginUserRequest.getPassword()));

        if(!result.isSame()) {
            return new Response("/user/login_failed.html");
        }

        Cookie cookie = Cookie.create("logined", "true");
        Response response = new Response("redirect:/");
        response.addCookie(cookie);
        return response;
    }
}
