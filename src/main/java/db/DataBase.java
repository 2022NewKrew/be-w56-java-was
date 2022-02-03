package db;

import com.google.common.collect.Maps;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import model.User;

public class DataBase {

    private static final Map<String, User> users = Maps.newHashMap();

    private DataBase() {
    }

    public static void addUser(User user) {
        users.put(user.getUserId(), user);
    }

    public static Optional<User> findUserById(String userId) {
        return Optional.ofNullable(users.get(userId));
    }

    public static List<User> findAll() {
        return new ArrayList<>(users.values());
    }
}
