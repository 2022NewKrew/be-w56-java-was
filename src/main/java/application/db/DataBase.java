package application.db;

import application.domain.User;
import com.google.common.collect.Maps;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

    public static List<User> findAll() {
        return new ArrayList<User>(users.values());
    }
}
