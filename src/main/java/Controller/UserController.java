package Controller;

import db.DataBase;
import model.LoginInfo;
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

        logger.debug("[POST] 회원가입 : {}", newUser);
    }

    public boolean login(Map<String, String> userInfo) {
        LoginInfo loginInfo = new LoginInfo(userInfo.get("userId"), userInfo.get("password"));
        User user = DataBase.findUserById(loginInfo.getUserId());
        if (user == null) {
            return false;
        }
        return loginInfo.getPassword().equals(user.getPassword());
    }
}
