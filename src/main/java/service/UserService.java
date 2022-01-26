package service;

import db.DataBase;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserService {
    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    public void join(User user) {
        DataBase.addUser(user);
        log.debug("User DataBase Status: {}", DataBase.findAll());
    }
}
