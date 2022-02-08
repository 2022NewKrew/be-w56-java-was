package webserver.service;

import db.DataBase;
import lombok.extern.slf4j.Slf4j;
import model.User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Slf4j
public class UserService {

    private static final UserService userService = new UserService();

    private UserService() {

    }

    public static UserService getInstance() {
        return userService;
    }

    public User joinUser(User user) {
        DataBase.addUser(user);
        log.info("회원가입 성공 : {}", user.getUserId());
        return user;
    }

    public boolean loginUser(String userId, String password) {
        User user = DataBase.findUserById(userId);
        if (user == null)
            return false;
        return user.getPassword().equals(password);
    }

    public List<User> getAllUsers() {
        Collection<User> users = DataBase.findAll();
        return new ArrayList<>(users);
    }
}
