package db;

import java.util.Collection;
import java.util.Map;

import com.google.common.collect.Maps;

import java.util.Optional;
import model.User;

public class DataBase {

    private static final Map<String, User> users = Maps.newHashMap();

    private DataBase() {}

    public static void addUser(User user) {
        users.put(user.getUserId(), user);
    }

    public static Optional<User> findUserById(String userId) {
        return Optional.of(users.get(userId));
    }

    public static Collection<User> findAll() {
        return users.values();
    }
}
