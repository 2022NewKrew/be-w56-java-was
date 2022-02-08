package webserver.application.service;

import webserver.application.db.DataBase;
import webserver.application.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class UserService {

    private static UserService instance = new UserService();

    private UserService() {
    }

    public static UserService getInstance() {
        return instance;
    }

    public void joinNewUser(User user) {
        DataBase.addUser(user);
    }

    public boolean login(String id, String password) {
        User user = DataBase.findUserById(id);
        return user != null && password.equals(user.getPassword());
    }

    public List<User> getAllUser(){
        return new ArrayList<>(DataBase.findAll());
    }
}
