package web.controller;

import annotation.Controller;
import annotation.GetMapping;
import annotation.PostMapping;
import db.DataBase;
import http.Cookie;
import model.User;
import web.dto.LoginDto;

import java.util.Optional;

@Controller
public class UserController {

    @GetMapping("/")
    public String index() {
        return "/index.html";
    }

    @PostMapping("/user/create")
    public String createUsers(User user) {
        DataBase.addUser(user);
        return "redirect:/";
    }

    @PostMapping("/user/login")
    public String login(LoginDto loginDto, Cookie cookie) {
        Optional<User> user = DataBase.findUserById(loginDto.getUserId());
        if (user.isPresent() && user.get().getPassword().equals(loginDto.getPassword())) {
            cookie.setCookie("logined", true);
            cookie.setCookie("Path", "/");
            return "redirect:/";
        }
        cookie.setCookie("logined", false);
        return "redirect:/user/login.html";
    }

    @GetMapping("/user/logout")
    public String logout(Cookie cookie) {
        cookie.setCookie("logined", false);
        cookie.setCookie("Path", "/");
        return "redirect:/";
    }
}
