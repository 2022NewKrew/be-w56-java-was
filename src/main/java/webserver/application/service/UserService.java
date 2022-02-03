package webserver.application.service;

import webserver.application.db.DataBase;
import webserver.application.model.User;

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
}
