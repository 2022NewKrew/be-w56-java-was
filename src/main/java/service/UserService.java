package service;

import db.DataBase;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

public class UserService {
    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    public static void join(User user) {
        DataBase.addUser(user);
        log.debug("User DataBase Status: {}", DataBase.findAll());
    }

    public static boolean login(String userId, String password) {
        log.debug("User DataBase Status: {}", DataBase.findAll());
        log.debug("userId: {}, password: {}", userId, password);
        User user = DataBase.findUserById(userId);
        if (user == null) {
            return false;
        }
        return Objects.equals(user.getPassword(), password);


    }
}
