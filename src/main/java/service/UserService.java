package service;

import db.DataBase;
import exception.IllegalCreateUserException;
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

    public void createUser(UserRequest userRequest) throws IllegalCreateUserException{
        User user = getUserFromUserRequest(userRequest);
        if(dataBase.findUserById(user.getUserId()).isEmpty())
            dataBase.addUser(user);
        else
            throw new IllegalCreateUserException("이미 존재하는 id 입니다.");
    }

    private User getUserFromUserRequest(UserRequest userRequest) {
        String userId = userRequest.getUserId();
        String password = userRequest.getPassword();
        String name = userRequest.getName();
        String email = userRequest.getEmail();

        return new User(userId, password, name, email);
    }
}
