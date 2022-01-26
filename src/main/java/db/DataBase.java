package db;

import java.util.Collection;
import java.util.Map;

import com.google.common.collect.Maps;

import lombok.extern.slf4j.Slf4j;
import model.User;

@Slf4j
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
}
