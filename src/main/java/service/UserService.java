package service;

import java.util.Map;

import db.DataBase;
import model.User;

public class UserService {
    private final String FRONT_NAME_USER_ID = "userId";
    private final String FRONT_NAME_PASSWORD = "password";
    private final String FRONT_NAME_NAME = "name";
    private final String FRONT_NAME_EMAIL = "email";

    private final DataBase dataBase;

    public UserService() {
        this.dataBase = new DataBase();
    }

    public boolean create(Map<String, String> parameters) {
        User user = new User(
                parameters.get(FRONT_NAME_USER_ID),
                parameters.get(FRONT_NAME_PASSWORD),
                parameters.get(FRONT_NAME_NAME),
                parameters.get(FRONT_NAME_EMAIL)
        );

        dataBase.addUser(user);

        return true;
    }

    public boolean login(Map<String, String> parameters) {
        User user;
        user = dataBase.getUser(parameters.get(FRONT_NAME_USER_ID));

        if (user.getPassword().equals(parameters.get(FRONT_NAME_PASSWORD))) {
            return true;
        }
        return false;
    }
}
