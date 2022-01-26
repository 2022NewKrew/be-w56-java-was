package controller;

import db.DataBase;
import dto.UserSignupRequest;
import http.PostMapping;
import model.User;

@Controller
public class UserController {

    @PostMapping("/user")
    public String signup(UserSignupRequest request) {
        User user = request.toEntity();
        DataBase.addUser(user);
        return "redirect:/index.html";
    }
}
