package web.controller;

import annotation.Controller;
import annotation.GetMapping;
import annotation.PostMapping;
import db.DataBase;
import http.header.Cookie;
import model.User;
import servlet.ServletResponse;
import servlet.view.Model;
import web.dto.LoginDto;

import java.util.Collection;
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
    public String login(LoginDto loginDto, ServletResponse response) {
        Optional<User> user = DataBase.findUserById(loginDto.getUserId());
        if (user.isPresent() && user.get().getPassword().equals(loginDto.getPassword())) {
            Cookie cookie = new Cookie("logined", "true");
            cookie.setPath("/");
            response.setCookie(cookie);
            return "redirect:/";
        }
        return "redirect:/user/login.html";
    }

    @GetMapping("/user/logout")
    public String logout(ServletResponse response) {
        Cookie cookie = new Cookie("logined", "false");
        cookie.setPath("/");
        response.setCookie(cookie);
        return "redirect:/";
    }

    @GetMapping("/user/list")
    public String userLists(Model model) {
        Collection<User> users = DataBase.findAll();
        model.setAttribute("users", users);
        return "/user/list.html";
    }
}
