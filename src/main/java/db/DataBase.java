package db;

import java.util.Collection;
import java.util.Map;

import com.google.common.collect.Maps;

import model.user.User;
import model.user.UserId;

public class DataBase {
    private static final Map<UserId, User> users = Maps.newHashMap();

    public static void addUser(User user) {
        users.put(user.getUserId(), user);
    }

    public static User findUserById(UserId userId) {
        return users.get(userId);
    }

    public static Collection<User> findAll() {
        return users.values();
    }
}
