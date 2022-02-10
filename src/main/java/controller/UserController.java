package controller;

import db.DataBase;
import model.User;
import webserver.RequestHandler;
import webserver.annotation.Controller;
import webserver.annotation.GetMapping;
import webserver.annotation.PostMapping;
import webserver.annotation.RequestParam;
import webserver.domain.Cookie;
import webserver.domain.Model;

import java.util.stream.Collectors;

@Controller
public class UserController {
    @PostMapping("/user/create")
    public String signUp(@RequestParam("userId") String userId,
                         @RequestParam("password") String password,
                         @RequestParam("name") String name,
                         @RequestParam("email") String email,
                         Cookie cookie) {
        DataBase.addUser(new User(userId, password, name, email));
        RequestHandler.log.debug(new User(userId, password, name, email).toString());
        return "redirect:/";
    }

    @PostMapping("/user/login")
    public String login(@RequestParam("userId") String userId,
                        @RequestParam("password") String password,
                        Cookie cookie) {
        User user = DataBase.findUserById(userId);
        if (user != null && user.getPassword().equals(password)) {
            cookie.addData("logined", "true");
            return "redirect:/";
        }
        cookie.addData("logined", "false");
        return "redirect:/user/login_failed.html";
    }

    @GetMapping("/user/list")
    public String userList(Cookie cookie, Model model) {
        String logined = cookie.getData("logined");
        if(logined == null || logined.equals("false")) {
            return "redirect:/user/login.html";
        }
        model.addAttribute("users", DataBase.findAllUsers().stream().collect(Collectors.toList()));
        return "/user/list";
    }
}
