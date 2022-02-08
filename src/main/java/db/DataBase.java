package db;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import model.User;
import model.UserLogin;

public class DataBase {

    private static final Map<String, User> users = new ConcurrentHashMap<>();

    private DataBase() {}

    public static void addUser(User user) {
        users.put(user.getUserId(), user);
    }

    public static User findUserById(String userId) {
        return users.get(userId);
    }

    public static Collection<User> findAll() {
        return users.values();
    }

    public static boolean login(UserLogin userLogin) {
        User user = findUserById(userLogin.getUserId());
        return user != null && userLogin.getPassword().equals(user.getPassword());
    }
}
