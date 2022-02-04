package service;

import com.google.common.base.Strings;
import dao.UserDao;
import db.DataBase;
import model.User;
import org.slf4j.Logger;
import util.LoginUtils;

import java.io.IOException;
import java.util.Map;

public class RequestService {

    private UserDao userDao;

    public RequestService(UserDao userDao) {
        this.userDao = userDao;
    }

    public void createUser(Map<String, String> params, Logger log) throws IOException {
        String userId = params.get("userId");
        String password = params.get("password");
        String name = params.get("name");
        String email = params.get("email");
        if(Strings.isNullOrEmpty(userId) || Strings.isNullOrEmpty(password) ||
                Strings.isNullOrEmpty(name) || Strings.isNullOrEmpty(email))
            throw new IOException("Parameter Not Exist");
        User user = new User(params.get("userId"), params.get("password"), params.get("name"), params.get("email"));
        log.debug("User : {}", user);
        //DataBase.addUser(user);
        userDao.addUser(user);
    }

    public String userLogin(Map<String, String> params, Logger log) throws IOException {
        String userId = params.get("userId");
        String password = params.get("password");
        if(Strings.isNullOrEmpty(userId) || Strings.isNullOrEmpty(password))
            throw new IOException("Parameter Not Exist");
        log.debug("userId : {}, password : {}", userId, password);
        //User user = DataBase.findUserById(userId);
        User user = userDao.findUserById(userId);
        String cookie = LoginUtils.checkLogin(log, user, password);
        return cookie;
    }

}
