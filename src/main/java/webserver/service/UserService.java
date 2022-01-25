package webserver.service;

import db.DataBase;
import model.User;

public class UserService {
    public void join(User user){
        if(DataBase.findUserById(user.getUserId()) != null){
            throw new IllegalArgumentException();
        }

        DataBase.addUser(user);
    }
}
