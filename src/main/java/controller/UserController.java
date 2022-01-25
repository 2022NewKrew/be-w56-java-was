package controller;

import db.DataBase;
import dto.UserCreateRequestDTO;

public class UserController {
    private static UserController instance = new UserController();

    private UserController() {
    }

    public static UserController getInstance() {
        return instance;
    }

    public void create(UserCreateRequestDTO userCreateRequestDTO) {
        DataBase.addUser(userCreateRequestDTO.toEntity());
    }
}
