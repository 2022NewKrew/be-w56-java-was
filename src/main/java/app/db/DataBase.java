package app.db;

import app.dto.UserLoginInfo;
import app.model.User;
import com.google.common.collect.Maps;

import java.util.Collection;
import java.util.Map;

public class DataBase {
    private static Map<String, User> users = Maps.newHashMap();

    public static void addUser(User user) {
        users.put(user.getUserId(), user);
    }

    public static User findUserById(String userId) {
        return users.get(userId);
    }

    public static User findUserByLoginInfo(UserLoginInfo userLoginInfo) {
        User user = users.get(userLoginInfo.getUserId());
        if (user == null) {
            return null;
        }

        if (!user.getPassword().equals(userLoginInfo.getPassword())){
            return null;
        }

        return user;
    }

    public static Collection<User> findAll() {
        return users.values();
    }
}
