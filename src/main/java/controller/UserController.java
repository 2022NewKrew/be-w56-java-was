package controller;

import db.DataBase;
import model.User;

import java.util.Map;

public class UserController implements Controller {

    @Override
    public String doGet(Map<String, String> param) {
        User user = new User(param.get("userId"), param.get("password"), param.get("name"), param.get("email"));
        DataBase.addUser(user);
        return "redirect:/index.html";
    }

    @Override
    public String doPost(Map<String, String> param, Map<String, String> body) {
        User user = new User(body.get("userId"), body.get("password"), body.get("name"), body.get("email"));
        DataBase.addUser(user);
        return "redirect:/index.html";

    }
}
