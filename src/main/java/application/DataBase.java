package application.db;

import java.util.Collection;
import java.util.Map;

import com.google.common.collect.Maps;

import application.domain.User;

public class DataBase {

    private static Map<String, User> users = Maps.newHashMap();

    public static void addUser(User user) {
        users.put(user.getUserId(), user);
    }

    static {
        User user = new User("testID", "1234", "김민수", "raon.su@kakaocorp.com");
        addUser(user);
    }

    public static User findUserById(String userId) {
        return users.get(userId);
    }

    public static Collection<User> findAll() {
        return users.values();
    }
}
