package service;

import db.DataBase;
import model.User;

import java.util.Map;

public class UserService {

    private static final UserService INSTANCE;

    static {
        INSTANCE = new UserService();
    }

    public static UserService getInstance() {
        return INSTANCE;
    }

    public void createUser(Map<String, String> queryParams) {
        User newUser = User.builder()
                .userId(queryParams.get("userId"))
                .password(queryParams.get("password"))
                .name(queryParams.get("name"))
                .email(queryParams.get("email"))
                .build();

        DataBase.addUser(newUser);
    }
}
