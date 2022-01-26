package db;

import java.util.Collection;
import java.util.Map;

import com.google.common.collect.Maps;

import lombok.extern.slf4j.Slf4j;
import model.User;

@Slf4j
public class DataBase {
    private DataBase() {

    }

    private static final Map<String, User> users = Maps.newHashMap();

    public static void addUser(User user) {
        users.put(user.getUserId(), user);
        log.info("SIGNUP: " + user);
    }

    public static boolean isExistUserId(String userId) {
        return users.containsKey(userId);
    }

    public static User findUserById(String userId) {
        return users.get(userId);
    }

    public static Collection<User> findAll() {
        return users.values();
    }
}
