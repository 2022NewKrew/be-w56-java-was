package service;

import db.DataBase;
import model.User;
import webserver.dto.UserRequest;

public class UserService {
    private final DataBase dataBase;
    private static final UserService userService = new UserService();

    private UserService() {
        dataBase = DataBase.getInstance();
    }

    public static UserService getInstance() {
        return userService;
    }

    public void createUser(UserRequest userRequest) {
        dataBase.addUser(getUserFromUserRequest(userRequest));
    }

    private User getUserFromUserRequest(UserRequest userRequest) {
        String userId = userRequest.getUserId();
        String password = userRequest.getPassword();
        String name = userRequest.getName();
        String email = userRequest.getEmail();

        return new User(userId, password, name, email);
    }
}
