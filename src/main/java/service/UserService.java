package service;

import db.DataBase;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class UserService {
    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    public static User createUser(Map<String, String> queries){
        User user = new User(
                queries.get("userId"), queries.get("password"), queries.get("name"), queries.get("email")
        );
        DataBase.addUser(user);
        log.info("[USER_SERVICE] : " + user);
        return user;
    }
}
