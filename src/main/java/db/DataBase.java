package db;

import java.util.Collection;
import java.util.Map;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;

import model.User;

public class DataBase {
    private static Map<String, User> users = Maps.newHashMap(ImmutableMap.of(
            "ryan", new User("ryan", "ww", "라이언", "hihi@hihi.com"),
            "prodo", new User("prodo", "ww", "프로도", "hihihi@hihi.com")
    ));

    public static void addUser(User user) {
        users.put(user.getUserId(), user);
    }

    public static User findUserById(String userId) {
        return users.getOrDefault(userId, null);
    }

    public static Collection<User> findAll() {
        return users.values();
    }
}
