package service;

import db.DataBase;
import service.dto.EnrollUserCommand;

public final class UserService {

    private UserService () { }

    public static void enroll(EnrollUserCommand command) {
        DataBase.addUser(command.toEntity());
    }

}
