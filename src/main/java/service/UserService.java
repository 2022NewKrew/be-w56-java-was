package service;

import db.DataBase;
import model.Request;
import model.User;

import java.util.Map;

public class UserService {
    
    public void save(Request request) {
        Map<String, String> queryString = request.getBody();
        User user = User.builder()
                .userId(queryString.get("userId"))
                .password(queryString.get("password"))
                .name(queryString.get("name"))
                .email(queryString.get("email"))
                .build();
        DataBase.addUser(user);
    }
}
