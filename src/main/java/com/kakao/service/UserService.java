package com.kakao.service;

import com.kakao.db.DataBase;
import com.kakao.model.User;

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

    private static final class InstanceHolder {
        private static final UserService instance = new UserService();
    }
}
