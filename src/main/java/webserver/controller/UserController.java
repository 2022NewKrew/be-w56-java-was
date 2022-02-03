package webserver.controller;

import dto.UserCreateDto;
import service.UserService;
import util.annotation.RequestMapping;
import webserver.Request;

public class UserController {
    UserService userService = new UserService();
    private static UserController instance = new UserController();

    private UserController() {}

    public static UserController getInstance() {
        return instance;
    }


    @RequestMapping(value="/users/create", method="GET")
    public String createByGet(Request request) {
        userService.create(UserCreateDto.builder()
                .stringId(request.getParameters().get("stringId"))
                .password(request.getParameters().get("password"))
                .name(request.getParameters().get("name"))
                .email(request.getParameters().get("email"))
                .build());
        return "redirect:/index.html";
    }

    public String controlByPost(Request request) {
        userService.create(UserCreateDto.builder()
                .stringId(request.getBodyAttributes().get("stringId"))
                .password(request.getBodyAttributes().get("password"))
                .name(request.getBodyAttributes().get("name"))
                .email(request.getBodyAttributes().get("email"))
                .build());
        return "redirect:/index.html";
    }
}
