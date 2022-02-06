package app.service;

import app.db.DataBase;
import app.dto.UserLoginInfo;
import app.model.User;

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
}
