package db;

import java.util.Collection;
import java.util.Map;

import com.google.common.collect.Maps;

import model.User;

public class DataBase {
    private static Map<String, User> users = Maps.newHashMap();

    static {
        User user = new User("jaden.dev", "123", "허홍준", "jaden.dev@kakaocorp.com");
        users.put(user.getUserId(), user);
        user = new User("brandon.jw", "123", "허진욱", "brandon.jw@kakaocorp.com");
        users.put(user.getUserId(), user);
        user = new User("shy.yi", "123", "이수호", "shy.yi@kakaocorp.com");
        users.put(user.getUserId(), user);
    }

    public static void addUser(User user) {
        users.put(user.getUserId(), user);
    }

    public static User findUserById(String userId) {
        return users.get(userId);
    }

    public static Collection<User> findAll() {
        return users.values();
    }
}
