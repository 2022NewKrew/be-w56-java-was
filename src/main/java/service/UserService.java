package service;

import db.DataBase;
import model.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by melodist
 * Date: 2022-02-07 007
 * Time: 오후 7:22
 */
public class UserService {

    public static List<User> getUserList() {
        return new ArrayList<>(DataBase.findAll());
    }
}
