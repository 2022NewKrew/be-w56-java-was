package service;

import db.DataBase;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.RequestHandler;

public class UserService {
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

    public static boolean loginValidation (String id, String password) {
        User user = DataBase.findUserById(id);
        if (user != null)
            return user.getPassword().equals(password);
        return false;
    }
}
