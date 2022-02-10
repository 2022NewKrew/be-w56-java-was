package service;

import db.UserStorage;
import java.sql.SQLException;
import java.util.ArrayList;
import model.User;

public class GetUserListService {

    public static ArrayList<User> getUserList() throws SQLException, ClassNotFoundException {
        return new ArrayList<>(UserStorage.findAll());
    }
}
