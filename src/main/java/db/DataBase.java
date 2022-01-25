package db;

import java.util.Collection;
import java.util.Map;

import com.google.common.collect.Maps;

import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DataBase {
    private static Map<String, User> users = Maps.newHashMap();
    private static Logger logger = LoggerFactory.getLogger(DataBase.class);

    public static void addUser(User user) {
        logger.debug(String.format("created user  %s, %s, %s, %s: ", user.getUserId(),
                                                                user.getPassword(),
                                                                user.getName(),
                                                                user.getEmail()));

        users.put(user.getUserId(), user);
    }

    public static User findUserById(String userId) {
        return users.get(userId);
    }

    public static Collection<User> findAll() {
        return users.values();
    }
}
