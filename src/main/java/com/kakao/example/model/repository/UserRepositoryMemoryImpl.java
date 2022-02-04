package com.kakao.example.model.repository;

import com.kakao.example.model.domain.User;
import framework.util.annotation.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static framework.util.annotation.Component.ComponentType.REPOSITORY;

@Component(type = REPOSITORY)
public class UserRepositoryMemoryImpl implements UserRepository {
    private static final Map<String, User> USERS = new ConcurrentHashMap<>();

    @Override
    public void addUser(User user) {
        USERS.put(user.getUserId(), user);
    }

    @Override
    public Optional<User> findUserById(String userId) {
        return Optional.ofNullable(USERS.get(userId));
    }

    @Override
    public Optional<User> findUserByLoginInfo(String userId, String password) {
        User user = USERS.get(userId);

        if (user == null || !user.getPassword().equals(password)) {
            return Optional.empty();
        }

        return Optional.of(user);
    }

    @Override
    public List<User> findAll() {
        return new ArrayList<>(USERS.values());
    }
}
