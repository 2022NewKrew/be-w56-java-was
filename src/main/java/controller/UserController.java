package controller;

import db.DataBase;
import http.request.HttpRequest;
import lombok.extern.slf4j.Slf4j;
import model.User;

@Slf4j
public class UserController {

    public static String createUser(HttpRequest request) {
        String userId = request.getParam("userId");
        String password = request.getParam("password");
        String name = request.getParam("name");
        String email = request.getParam("email");
        User user = new User(userId, password, name, email);
        DataBase.addUser(user);

        log.info("user created");
        return "/index.html";
    }
}
