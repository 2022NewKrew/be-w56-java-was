package application.db;

import java.util.*;

import com.google.common.collect.Maps;

import application.model.User;

public class DataBase {

    private static Map<String, User> users = Maps.newHashMap();

    static {
        users.put("1234", new User("1234", "1234", "1234", "1234@1234"));
    }

    public static void addUser(User user) {
        users.put(user.getUserId(), user);
    }

    public static Optional<User> findUserById(String userId) {
        return Optional.ofNullable(users.get(userId));
    }

    public static List<User> findAll() {
        return new ArrayList<>(users.values());
    }
}
