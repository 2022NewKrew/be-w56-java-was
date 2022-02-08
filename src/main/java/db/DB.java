package db;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import http.exception.NotFound;
import model.User;

public class DB {

    private static final Map<String, User> users = new ConcurrentHashMap<>();

    public static void addUser(User user) {
        users.put(user.getUserId(), user);
    }

    public static User findUserById(String userId) {
        return users.get(userId);
    }

    public static User findUserByIdAndPassword(String userId, String password) {
        return users.entrySet()
                .stream()
                .filter(entry -> entry.getKey().equals(userId))
                .filter(entry -> entry.getValue().getPassword().equals(password))
                .findAny()
                .map(Map.Entry::getValue)
                .orElseThrow(NotFound::new);
    }

    public static Collection<User> findAll() {
        return users.values();
    }
}
