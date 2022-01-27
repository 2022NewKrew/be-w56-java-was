package controller;

import db.DataBase;
import model.User;
import webserver.RequestHandler;
import webserver.annotation.Controller;
import webserver.annotation.PostMapping;
import webserver.annotation.RequestParam;
import webserver.domain.Cookie;

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
}
