package service.auth;

import db.DataBase;
import domain.user.User;

import java.util.ArrayList;
import java.util.List;

public class UserListService {

    public List<User> getAllUsers() {
        return new ArrayList<>(DataBase.findAll());
    }

}
