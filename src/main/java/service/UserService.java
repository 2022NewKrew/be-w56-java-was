package service;

import dao.UserDao;
import db.MongoDb;
import model.User;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by melodist
 * Date: 2022-02-07 007
 * Time: 오후 7:22
 */
public class UserService {

    public static List<User> getUserList() {
        return MongoDb.findAll().stream()
                .map(UserDao::toEntity)
                .collect(Collectors.toList());
    }

    public static User getUserByUserId(String userId) {
        return MongoDb.findUserByUserId(userId).toEntity();
    }

    public static void addUser(String userId, String password, String name, String email) {
        UserDao userDao = new UserDao(userId, password, name, email);
        MongoDb.addUser(userDao);
    }
}
