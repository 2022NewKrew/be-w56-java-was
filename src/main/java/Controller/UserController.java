package Controller;

import db.DataBase;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    public void signUp(Map<String, String> userInfo) {
        User newUser = new User(userInfo.get("userId"),
                userInfo.get("password"),
                userInfo.get("name"),
                userInfo.get("email"));
        DataBase.addUser(newUser);

        logger.debug("[GET] 회원가입 : {}", newUser);
    }
}
