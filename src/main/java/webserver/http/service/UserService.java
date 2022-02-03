package webserver.http.service;

import db.DataBase;
import dto.UserLoginDto;
import dto.UserSignUpDto;
import entity.User;

import java.util.Collection;
import java.util.List;

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

    public User findUser(UserLoginDto userLoginDto) {
        return DataBase.findUserById(userLoginDto.getUserId());
    }

    public Collection<User> findUsers() {
        return DataBase.findAll();
    }
}
