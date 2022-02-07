package db;

import java.util.Collection;
import java.util.Map;

import com.google.common.collect.Maps;

import model.User;

public class DataBase {
    private static Map<String, User> users = Maps.newHashMap();
    private static final User dummyUser = new User("-1", "", "", "");

    public static void addUser(User user) {
        users.put(user.getUserId(), user);
    }

    public static User findUserById(String userId) {
        return users.getOrDefault(userId, dummyUser);
    }

    public static Collection<User> findAll() {
        return users.values();
    }
}
