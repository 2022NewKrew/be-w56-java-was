package service;

import db.DataBase;
import domain.User;
import service.dto.*;

import java.util.List;
import java.util.stream.Collectors;

public final class UserService {

    private UserService () { }

    public static void enroll(EnrollUserCommand command) {
        DataBase.addUser(command.toEntity());
    }

    public static LoginUserResult getUserInfo(LoginUserCommand command) {
        User user = DataBase.findUserById(command.getUserId());
        return new LoginUserResult(user.passwordIsSame(command.getPassword()));
    }

    public static GetAllUserInfoResult getAllUserInfo() {
        List<GetUserInfoResult> userInfos = DataBase.findAll().stream()
                .map(v -> new GetUserInfoResult(v.getUserId(), v.getEmail(), v.getName()))
                .collect(Collectors.toList());

        return new GetAllUserInfoResult(userInfos);
    }
}
