package service;

import db.DataBase;
import java.util.Map;
import model.User;

public class UserService {

    private static UserService instance;

    private UserService() {
    }

    public static UserService getInstance() {
        if (instance == null) {
            instance = new UserService();
        }
        return instance;
    }

    public void register(Map<String, String> params) {
        User user = User.from(params);
        DataBase.addUser(user);
    }
}
