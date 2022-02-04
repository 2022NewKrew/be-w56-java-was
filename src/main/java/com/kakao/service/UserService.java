package com.kakao.service;

import com.kakao.db.DataBase;
import com.kakao.model.User;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Singleton
 */
public class UserService {
    private UserService() {
    }

    public static UserService getInstance() {
        return InstanceHolder.instance;
    }

    public void addUser(User user) {
        DataBase.addUser(user);
    }

    public Optional<User> findByUserId(String userId) {
        return Optional.ofNullable(DataBase.findUserById(userId));
    }

    public List<User> findAll() {
        return DataBase.findAll().stream()
                .sorted(Comparator.comparing(User::getUserId))
                .collect(Collectors.toUnmodifiableList());
    }

    private static final class InstanceHolder {
        private static final UserService instance = new UserService();
    }
}
