package com.kakao.example.application.service;

import com.kakao.example.application.dto.UserDto;
import com.kakao.example.model.domain.User;

import java.util.List;

public interface UserService {
    void addUser(User user);

    UserDto findUserById(String userId);

    UserDto findUserByLoginInfo(String userId, String password);

    List<UserDto> findAll();
}
