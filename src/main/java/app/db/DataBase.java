package app.db;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;

import com.google.common.collect.Maps;

import app.model.user.User;

public class DataBase {
    private static Map<String, User> users = Maps.newHashMap();

    static {
        users.put("spiderman", new User("spiderman","1234","peter","peter@gmail.com"));
        users.put("superman", new User("superman","1234","clark","peter@gmail.com"));
        users.put("ironman", new User("ironman","1234","tony","peter@gmail.com"));
    }

    public static void addUser(User user) {
        users.put(user.getUserId(), user);
    }

    public static Optional<User> findUserById(String userId) {
        return Optional.ofNullable(users.get(userId));
    }

    public static Collection<User> findAll() {
        return users.values();
    }
}
