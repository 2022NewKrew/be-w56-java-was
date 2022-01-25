package controller;

import db.DataBase;
import model.User;

public class AuthController {

    public AuthController() {
    }

    public static String signUp(String userId, String password, String name, String email) {
        User user = new User(userId, password, name, email);
        DataBase.addUser(user);
        return "/index.html";
    }
}
