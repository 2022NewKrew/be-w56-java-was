package service;

import db.DataBase;
import dto.UserCreateDto;

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
}
