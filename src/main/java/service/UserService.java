package service;

import db.JdbcUserRepository;
import domain.User;
import service.dto.*;

import java.util.List;
import java.util.stream.Collectors;

public final class UserService {

    private UserService() {
    }

    public static void enroll(EnrollUserCommand command) {
        JdbcUserRepository.addUser(command.toEntity());
    }

    public static LoginUserResult getUserInfo(LoginUserCommand command) {
        User user = JdbcUserRepository.findUserById(command.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("일치하지 않는 UserId 입니다"));
        return new LoginUserResult(user.passwordIsSame(command.getPassword()));
    }

    public static GetAllUserInfoResult getAllUserInfo() {
        List<GetUserInfoResult> userInfos = JdbcUserRepository.findAll().stream()
                .map(v -> new GetUserInfoResult(v.getUserId(), v.getEmail(), v.getName()))
                .collect(Collectors.toList());

        return new GetAllUserInfoResult(userInfos);
    }
}
