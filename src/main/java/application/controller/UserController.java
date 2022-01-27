package application.controller;

import application.db.DataBase;
import application.model.User;
import http.request.HttpRequest;

public class UserController {

    public static String create(String userId, String password, String name, String email) {
        User user = new User(userId, password, name, email);
        DataBase.addUser(user);
        return "redirect:/index.html";
    }

}
