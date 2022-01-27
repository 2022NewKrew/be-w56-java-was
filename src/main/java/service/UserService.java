package service;

import db.DataBase;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import http.request.Queries;

public class UserService {
    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    private static final UserService userService = new UserService();

    private UserService(){}

    public static UserService getInstance(){
        return userService;
    }


    public User createUser(Queries queries){
        User user = new User(
                queries.get("userId"), queries.get("password"), queries.get("name"), queries.get("email")
        );
        DataBase.addUser(user);
        log.info("[USER_SERVICE] : " + user);
        return user;
    }
}
