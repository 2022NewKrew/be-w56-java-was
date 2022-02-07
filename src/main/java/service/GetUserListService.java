package service;

import db.DataBase;
import java.util.ArrayList;
import model.User;

public class GetUserListService {

    public static ArrayList<User> getUserList() {
        return new ArrayList<>(DataBase.findAll());
    }
}
