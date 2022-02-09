package domain.db;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.google.common.collect.Maps;

import domain.model.User;

public class DataBase {
    private static Map<String, User> users = new HashMap<>() {{
        put("asdf", new User("asdf", "asdf", "name", "bluetooth@gmail.com"));
        put("test", new User("test", "test", "admin", "lsh@naver.com"));
    }};

    public static void addUser(User user) {
        users.put(user.getUserId(), user);
    }

    public static User findUserById(String userId) {
        return users.get(userId);
    }

    public static List<User> findAll() {
        return users.values().stream().collect(Collectors.toList());
    }
}
