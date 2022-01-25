package service;

import db.DataBase;
import http.Request;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.util.Map;

import static util.HttpRequestUtils.parseQueryString;

public class UserService {
    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    public void join(Request request) {
        Map<String, String> newUser = parseQueryString(request.getQueryString());
        User user = new User(newUser.get("userId"), newUser.get("password"), newUser.get("name"), newUser.get("email"));
        DataBase.addUser(user);
        log.debug("User DataBase Status: {}", DataBase.findAll());
    }
}
