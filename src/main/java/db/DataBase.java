package db;

import model.User;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

public class DataBase {
    private final Map<String, User> users = new LinkedHashMap<>();

    public void addUser(User user) {
        users.put(user.getId(), user);
    }

    public User findUserById(String userId) {
        return users.get(userId);
    }

    public Collection<User> findAll() {
        return users.values();
    }
}
