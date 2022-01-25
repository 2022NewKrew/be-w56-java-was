package service;

import db.DataBase;
import java.util.Map;
import model.User;

public class UserService {

    private UserService() {
    }

    public static void register(Map<String, String> queries) {
        User user = User.from(queries);
        DataBase.addUser(user);
    }
}
