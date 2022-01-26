package app.service;

import app.db.DataBase;
import app.model.User;

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

    public void signupUser(User user) {
        DataBase.addUser(user);
    }
}
