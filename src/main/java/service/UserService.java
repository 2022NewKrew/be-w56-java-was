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

    public Boolean login(User user) {
        User dbUser = DataBase.findUserById(user.getUserId());
        if(dbUser == null) return false;
        if(dbUser.getPassword().equals(user.getPassword())) return true;
        return false;
    }
}
