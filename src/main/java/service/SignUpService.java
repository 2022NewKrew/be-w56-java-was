package service;

import db.DataBase;
import java.util.Map;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SignUpService {

    private static final Logger log = LoggerFactory.getLogger(SignUpService.class);


    public static void signUp(Map<String, String> queryData) {

        User user = new User(
            queryData.get("userId"),
            queryData.get("password"),
            queryData.get("name"),
            queryData.get("email")
        );

        DataBase.addUser(user);

        log.debug("User {} {} {} signed up", user.getUserId(), user.getName(), user.getEmail());
    }
}
