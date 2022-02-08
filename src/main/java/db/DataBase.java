package db;

import java.util.Collection;
import java.util.Map;

import com.google.common.collect.Maps;

import model.User;

public class DataBase {

    private static Map<String, User> users = Maps.newHashMap();

    public static void addUser(User user) {
        users.put(user.getUserId(), user);
    }

    public static User findUserById(String userId) {
        return users.get(userId);
    }

    public static Collection<User> findAll() {
        return users.values();
    }

    static {
        User user1 = new User("FIRSTID", "1234", "hello", "hi@hi.com");
        User user2 = new User("SECONDID", "1234", "green", "bye@hi.com");
        addUser(user1);
        addUser(user2);
    }
}
