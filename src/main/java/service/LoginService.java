package service;

import db.DataBase;
import model.User;

import java.util.Map;

public class LoginService {
    public void join(Map<String, String> params) {
        User user = new User(params.get("userId"),params.get("password"), params.get("name"), params.get("email"));
        DataBase.addUser(user);
    }
}
