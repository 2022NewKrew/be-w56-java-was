package db;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;

import com.google.common.collect.Maps;

import model.User;

public class DataBase {
    private static Map<String, User> users = Maps.newHashMap();

    public static void addUser(User user) {
        users.put(user.getUserId(), user);
    }

    public static Optional<User> findUserById(String userId) {
        try {
            return Optional.of(users.get(userId));
        } catch (NullPointerException e) {
            return Optional.empty();
        }
    }

    public static Collection<User> findAll() {
        return users.values();
    }
}
