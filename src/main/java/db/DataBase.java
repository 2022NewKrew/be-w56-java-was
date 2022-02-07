package db;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import model.User;
import model.UserLogin;

public class DataBase {

    private static final Map<String, User> USERS = new ConcurrentHashMap<>();

    private DataBase() {}

    public static void addUser(User user) {
        USERS.put(user.getUserId(), user);
    }

    public static User findUserById(String userId) {
        return USERS.get(userId);
    }

    public static Collection<User> findAll() {
        return USERS.values();
    }

    public static boolean login(UserLogin userLogin) {
        User user = findUserById(userLogin.getUserId());
        return user != null && userLogin.getPassword().equals(user.getPassword());
    }
}
