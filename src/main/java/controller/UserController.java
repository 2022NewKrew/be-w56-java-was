package controller;

import dto.UserCreateDto;
import dto.UserLoginDto;
import http.request.HttpRequest;
import http.response.HttpResponse;
import model.User;
import service.UserService;
import webserver.Model;
import webserver.annotation.Controller;
import webserver.annotation.GetMapping;
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

    @PostMapping("/users/login")
    public String login(HttpRequest request, HttpResponse response) {
        UserLoginDto userLoginDto = UserLoginDto.from(request.getBody());
        User user = userService.login(userLoginDto);
        if (user == null) {
            return "redirect:/users/login_failed.html";
        }

        response.setCookie("logined=true; Path=/");

        return "redirect:/index.html";
    }

    @GetMapping("/users/logout")
    public String logout(HttpRequest request, HttpResponse response) {
        response.setCookie("logined=false; Path=/");

        return "redirect:/index.html";
    }

    @GetMapping("/users/list")
    public String getAllUsers(HttpRequest request, HttpResponse response, Model model) {
        boolean isLogin = userService.isLogin(request);
        if (!isLogin) {
            return "redirect:/users/login.html";
        }

        model.addAttribute("users", userService.getAllUsers());
        return "/users/list.html";
    }
}
