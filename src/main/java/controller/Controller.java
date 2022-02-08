package controller;

import annotation.*;
import controller.dto.EnrollUserRequest;
import controller.dto.LoginUserRequest;
import model.Model;
import service.UserService;
import service.dto.EnrollUserCommand;
import service.dto.GetAllUserInfoResult;
import service.dto.LoginUserCommand;
import service.dto.LoginUserResult;
import webserver.http.Cookie;
import webserver.http.Response;


public class Controller {

    @GetMapping(path = "/")
    public Response getIndex() {
        return new Response("/index.html", 200);
    }

    @GetMapping(path = "/user/create")
    public Response signUp(
            @RequestParam(name = "userId") String userId,
            @RequestParam(name = "password") String password,
            @RequestParam(name = "name") String name,
            @RequestParam(name = "email") String email) {

        UserService.enroll(new EnrollUserCommand(userId, password, name, email));

        return new Response("/", 302);
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

        return new Response("/", 302);
    }

    @PostMapping(path = "/user/login")
    public Response login(@RequestBody(names = {"userId", "password"}) LoginUserRequest loginUserRequest) {

        LoginUserResult result = UserService.getUserInfo(
                new LoginUserCommand(
                        loginUserRequest.getUserId(),
                        loginUserRequest.getPassword()));

        if(!result.isSame()) {
            Response failResponse = new Response("/user/login_failed.html", 200);
            failResponse.addCookie(Cookie.create("logined", "false"));
            return failResponse;
        }

        Cookie cookie = Cookie.create("logined", "true");
        Response response = new Response("/", 302);
        response.addCookie(cookie);
        return response;
    }

    @GetMapping(path = "/user/list")
    public Response userList(@HttpCookie(name = "logined") String login) {

        GetAllUserInfoResult allUserInfo = UserService.getAllUserInfo();

        if(login.equals("false") || login.equals("")) {
            return new Response("/user/login.html", 200);
        }

        Model model = Model.create();
        model.addAttribute("userList", allUserInfo.getUserInfos());
        Response response = new Response("/userList", 200);
        response.setModel(model);
        return response;
    }
}
