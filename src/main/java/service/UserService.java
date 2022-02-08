package service;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Map;

import db.DataBase;
import model.User;

public class UserService {
    private static final String FRONT_NAME_USER_ID = "userId";
    private static final String FRONT_NAME_PASSWORD = "password";
    private static final String FRONT_NAME_NAME = "name";
    private static final String FRONT_NAME_EMAIL = "email";

    public UserService() {}

    public boolean create(Map<String, String> parameters) {
        User user = new User(
                parameters.get(FRONT_NAME_USER_ID),
                parameters.get(FRONT_NAME_PASSWORD),
                parameters.get(FRONT_NAME_NAME),
                URLDecoder.decode(parameters.get(FRONT_NAME_EMAIL), StandardCharsets.UTF_8)
        );

        DataBase.addUser(user);

        return true;
    }

    public boolean login(Map<String, String> parameters) {
        User user;
        user = DataBase.getUser(parameters.get(FRONT_NAME_USER_ID));

        return user.getPassword().equals(parameters.get(FRONT_NAME_PASSWORD));
    }

    public Collection<User> listAll() {
        return DataBase.findAll();
    }
}
