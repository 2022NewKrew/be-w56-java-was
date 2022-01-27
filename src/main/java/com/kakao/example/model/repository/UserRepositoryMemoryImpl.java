package com.kakao.example.model.repository;

import com.kakao.example.model.domain.User;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class UserRepositoryMemoryImpl implements UserRepository {
    private static final Map<String, User> USERS = new ConcurrentHashMap<>();

    private static UserRepository instance;

    public UserRepositoryMemoryImpl() {}

    public static UserRepository getInstance() {
        instance = new UserRepositoryMemoryImpl();
        return instance;
    }

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
    public Collection<User> findAll() {
        return USERS.values();
    }
}
