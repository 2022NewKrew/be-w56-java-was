package service;

import db.DataBase;
import model.User;
import service.dto.EnrollUserCommand;
import service.dto.LoginUserCommand;
import service.dto.LoginUserResult;

public final class UserService {

    private UserService () { }

    public static void enroll(EnrollUserCommand command) {
        DataBase.addUser(command.toEntity());
    }

    public static LoginUserResult getUserInfo(LoginUserCommand command) {
        User user = DataBase.findUserById(command.getUserId());
        return new LoginUserResult(user.passwordIsSame(command.getPassword()));
    }
}
