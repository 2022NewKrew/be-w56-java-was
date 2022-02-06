package user.repository;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import user.domain.User;

public class DataBase {

    private static final Logger logger = LoggerFactory.getLogger(DataBase.class);

    private static final Map<String, User> users = new ConcurrentHashMap<>();

    public static void addUser(User user) {
        users.put(user.getUserId(), user);
    }

    public static Optional<User> findUserByUserId(String userId) {
        return Optional.ofNullable(users.get(userId));
    }

    public static Collection<User> findAll() {
        return users.values();
    }
}
