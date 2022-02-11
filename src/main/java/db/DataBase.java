package db;

import com.google.common.collect.Maps;
import java.util.Collection;
import java.util.Map;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DataBase {

    private static final Logger log = LoggerFactory.getLogger(DataBase.class);
    private static Map<String, User> users = Maps.newConcurrentMap();


    public static void addUser(User user) {
        users.put(user.getUserId(), user);
        log.debug("user {} added", user.getUserId());
    }

    public static User findUserById(String userId) {
        return users.get(userId);
    }

    public static Collection<User> findAll() {
        users.clear();
        users.put("miya.ong", new User("miya.ong", "1234", "박예지", "miya.ong@kakaocorp.com"));
        users.put("bow.wow", new User("bow.wow", "1234", "김댕댕", "bow.wow@kakaocorp.com"));
        return users.values();
    }
}
