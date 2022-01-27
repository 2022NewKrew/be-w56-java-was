package service;

import db.DataBase;
import model.User;


public enum UserService {
    INSTANCE;

    public void addUser(User user) {
        DataBase.addUser(user);
    }

    public boolean userLogin(String userId, String password) {
        User user = DataBase.findUserById(userId);
        return user != null && user.getPassword().equals(password);
    }
}
