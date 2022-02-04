package com.kakao.example.model.repository;

import com.kakao.example.model.domain.User;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface UserRepository {
    void addUser(User user);

    Optional<User> findUserById(String userId);

    Optional<User> findUserByLoginInfo(String userId, String password);

    List<User> findAll();
}
