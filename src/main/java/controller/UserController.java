package controller;

import db.DataBase;
import model.User;
import webserver.RequestHandler;
import webserver.annotation.Controller;
import webserver.annotation.PostMapping;
import webserver.annotation.RequestParam;

@Controller
public class UserController {
    @PostMapping("/user/create")
    public String signUp(@RequestParam("userId") String userId,
                         @RequestParam("password") String password,
                         @RequestParam("name") String name,
                         @RequestParam("email") String email) {
        DataBase.addUser(new User(userId, password, name, email));
        RequestHandler.log.debug(new User(userId, password, name, email).toString());
        return "redirect:/";
    }
}
