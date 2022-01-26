package service;

import db.DataBase;
import dto.UserSignUpDto;
import entity.User;

public class UserService {

    public void join(UserSignUpDto userSignUpDto) {
        User user = User.builder()
                .userId(userSignUpDto.getUserId())
                .password(userSignUpDto.getPassword())
                .name(userSignUpDto.getName())
                .email(userSignUpDto.getEmail())
                .build();

        DataBase.addUser(user);
    }
}
