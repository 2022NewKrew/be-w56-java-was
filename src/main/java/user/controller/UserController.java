package user.controller;

import db.DataBase;
import http.Request;
import model.User;

import java.util.Map;

public class UserController {

    //회원가입
    public static String createUser(Request request) {
        Map<String, String> elements = request.getElements();

        User user = new User(elements.get("userId"),
                            elements.get("password"),
                            elements.get("name"),
                            elements.get("email"));

        DataBase.addUser(user);

        return "redirect:/index.html";
    }
}
