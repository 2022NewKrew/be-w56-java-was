package service;

import db.DataBase;
import model.User;

public class UserService {

    private static UserService userService;

    private UserService(){
    }

    public static UserService getInstance(){
        if(userService == null){
            userService = new UserService();
        }
        return userService;
    }

    public void joinNewUser(User user){
        DataBase.addUser(user);
    }

}
