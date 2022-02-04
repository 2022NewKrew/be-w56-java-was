package controller;

import dto.UserCreateDto;
import http.request.HttpRequest;
import http.response.HttpResponse;
import service.UserService;
import webserver.annotation.Controller;
import webserver.annotation.PostMapping;

@Controller
public class UserController {

    private static UserController instance;
    private final UserService userService;

    public UserController() {
        userService = UserService.getInstance();
    }

    @PostMapping("/users/create")
    public String signUp(HttpRequest request, HttpResponse response) {
        UserCreateDto userCreateDto = UserCreateDto.from(request.getBody());
        userService.register(userCreateDto);
        return "redirect:/index.html";
    }
}
