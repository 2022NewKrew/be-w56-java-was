package service;

import db.DataBase;
import model.User;
import org.slf4j.Logger;
import util.LoginUtils;

import java.util.Map;

public class HttpService {

    public void createUser(Map<String, String> params, Logger log) {
        User user = new User(params.get("userId"), params.get("password"), params.get("name"), params.get("email"));
        log.debug("User : {}", user);
        DataBase.addUser(user);
    }

    public String userLogin(Map<String, String> params, Logger log) {
        String userId = params.get("userId");
        String password = params.get("password");
        log.debug("userId : {}, password : {}", userId, password);
        User user = DataBase.findUserById(userId);
        String cookie = LoginUtils.checkLogin(log, user, password);
        return cookie;
    }

}
