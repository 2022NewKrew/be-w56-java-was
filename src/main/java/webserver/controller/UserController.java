package webserver.controller;

import dto.UserCreateDto;
import service.UserService;
import util.annotation.RequestMapping;
import webserver.Request;

public class UserController {
    private static final UserController instance = new UserController();
    private static final UserService userService = UserService.getInstance();

    private UserController() {}

    public static UserController getInstance() {
        return instance;
    }


    @RequestMapping(value="/users/create", method="GET")
    public String createByGet(Request request) {
        userService.create(UserCreateDto.builder()
                .stringId(request.getParameter("stringId"))
                .password(request.getParameter("password"))
                .name(request.getParameter("name"))
                .email(request.getParameter("email"))
                .build());
        return "redirect:/index.html";
    }

    @RequestMapping(value="/users", method="POST")
    public String createByPost(Request request) {
        userService.create(UserCreateDto.builder()
                .stringId(request.getParameter("stringId"))
                .password(request.getParameter("password"))
                .name(request.getParameter("name"))
                .email(request.getParameter("email"))
                .build());
        return "redirect:/index.html";
    }
}
