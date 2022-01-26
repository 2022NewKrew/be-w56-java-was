package webserver.service;

import db.DataBase;
import lombok.extern.slf4j.Slf4j;
import model.User;

@Slf4j
public class UserService {

    private static UserService userService = new UserService();

    private UserService() {

    }

    public static UserService getInstance() {
        return userService;
    }

    public User joinUser(User user) {
        DataBase.addUser(user);
        log.info("회원가입 성공 : {}", user.getUserId().toString());
        return user;
    }

    public User loginUser(User user) {
        // TODO
        return null;
    }
}
