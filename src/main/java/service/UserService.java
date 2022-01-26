package service;

import db.DataBase;
import java.util.Map;
import model.User;

public class UserService {

    private UserService() {
    }

    public static void register(Map<String, String> params) {
        User user = User.from(params);
        DataBase.addUser(user);
    }
}
