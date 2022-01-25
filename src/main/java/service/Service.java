package service;

import db.DataBase;
import dto.UserCreateRequestDto;
import model.User;

public class Service {

    public void signUp(UserCreateRequestDto requestDto) {
        DataBase.addUser(new User(requestDto.getUserId(), requestDto.getPassword(), requestDto.getName(), requestDto.getEmail()));
    }
}
