package app.service;

import app.db.DataBase;
import app.dto.UserLoginInfo;
import app.model.User;

import java.util.ArrayList;
import java.util.List;

public class UserService {

    private static UserService instance;

    private UserService() {
    }

    public synchronized static UserService getInstance() {
        if (instance == null) {
            instance = new UserService();
        }
        return instance;
    }

    public void signupUser(User user) {
        DataBase.addUser(user);
    }

    public User loginUser(UserLoginInfo info) {
        return DataBase.findUserByLoginInfo(info);
    }

    public List<User> getUserList() {
        return new ArrayList<>(DataBase.findAll());
    }
}
