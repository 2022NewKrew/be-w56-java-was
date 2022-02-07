package db;

import com.google.common.collect.Maps;
import model.Request;
import model.User;

import java.util.Collection;
import java.util.Map;

public class RepositoryImpl {
    private static Map<String, User> users = Maps.newHashMap();

    private RepositoryImpl() {
        throw new IllegalStateException("Utility class");
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

    public static void save(Request request) {
        Map<String, String> queryString = request.getQueryString();
        User newUser = User.builder()
                .userId(queryString.get("userId"))
                .password(queryString.get("password"))
                .name(queryString.get("name"))
                .email(queryString.get("email"))
                .build();
        addUser(newUser);
    }
}
