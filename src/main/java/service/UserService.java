package service;

import db.DataBase;
<<<<<<< HEAD
=======
import http.Request;
>>>>>>> 8f589f5 (웹 서버 구현 2단계 (#120))
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

<<<<<<< HEAD
public class UserService {
    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    public void join(User user) {
=======

import java.util.Map;

import static util.HttpRequestUtils.parseQueryString;

public class UserService {
    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    public void join(Request request) {
        Map<String, String> newUser = parseQueryString(request.getQueryString());
        User user = new User(newUser.get("userId"), newUser.get("password"), newUser.get("name"), newUser.get("email"));
>>>>>>> 8f589f5 (웹 서버 구현 2단계 (#120))
        DataBase.addUser(user);
        log.debug("User DataBase Status: {}", DataBase.findAll());
    }
}
