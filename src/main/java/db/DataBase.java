package db;

import java.util.Collection;
import java.util.Map;

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

    public User findUserById(String userId) {
        return users.get(userId);
    }

    public Collection<User> findAll() {
        return users.values();
    }
}
