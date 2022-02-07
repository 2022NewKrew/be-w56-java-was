package webserver.service;

import db.DataBase;
import model.User;

import java.util.ArrayList;
import java.util.List;

public class UserService {
    public void join(User user) {
        if (DataBase.findUserById(user.getUserId()) != null) {
            throw new IllegalArgumentException();
        }

        DataBase.addUser(user);
    }

    public User findUser(String userId) {
        return DataBase.findUserById(userId);
    }

    public List<User> findAllUser(){
        return new ArrayList<>(DataBase.findAll());
    }
}
