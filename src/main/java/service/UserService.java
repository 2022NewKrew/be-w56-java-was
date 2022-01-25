package service;

import db.DataBase;
import model.User;


public enum UserService {
    INSTANCE;

    public void addUser(User user) {
        DataBase.addUser(user);
    }
}
