package service;

import db.DataBase;
import dto.UserCreateDto;
import dto.UserLoginDto;
import model.User;

public class UserService {

    private static UserService instance;

    private UserService() {
    }

    public static UserService getInstance() {
        if (instance == null) {
            instance = new UserService();
        }
        return instance;
    }

    public void register(UserCreateDto userCreateDto) {
        DataBase.addUser(userCreateDto.toEntity());
    }

    public User login(UserLoginDto userLoginDto) {
        User result = DataBase.findUserById(userLoginDto.getUserId());
        if (result == null || !result.getPassword().equals(userLoginDto.getPassword())) {
            return null;
        }

        return result;
    }
}
