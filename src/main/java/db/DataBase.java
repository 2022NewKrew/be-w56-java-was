package db;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;

import com.google.common.collect.Maps;

import model.User;

public class DataBase {

    private final Map<String, User> users;
    private static final DataBase dataBase = new DataBase();

    private DataBase() {
        users = Maps.newHashMap();
    }

    public static DataBase getInstance() {
        return dataBase;
    }

    public void addUser(User user) {
        users.put(user.getUserId(), user);
    }

    public Optional<User> findUserById(String userId) {
        if (users.containsKey(userId))
            return Optional.of(users.get(userId));
        else
            return Optional.empty();
    }

    public Collection<User> findAll() {
        return users.values();
    }
}
