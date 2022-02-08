package service;

import db.DataBase;
import model.User;

public class UserService {
    private static UserService instance;

    private UserService() {}

    public static synchronized UserService getInstance() {
        if(instance == null) {
            instance = new UserService();
        }
        return instance;
    }

    public void create(User user) {
        DataBase.addUser(user);
    }
}
