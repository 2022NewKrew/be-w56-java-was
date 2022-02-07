package db;

import java.util.Collection;
import java.util.Map;

import com.google.common.collect.Maps;

import model.user.*;

public class DataBase {
    private static final Map<UserId, User> users = Maps.newHashMap();

    public static void addUser(User user) {
        users.put(user.getUserId(), user);
    }

    public static User findUserById(UserId userId) {
        return users.get(userId);
    }

    public static Collection<User> findAll() {
        return users.values();
    }

    static {
        User user1 = new User(
                new UserId("david.dh"),
                new Password("1234"),
                new Name("김대환"),
                new Email("david.dh@kakaocorp.com")
        );

        User user2 = new User(
                new UserId("kdh5163"),
                new Password("1234"),
                new Name("데이비드"),
                new Email("kdh5163@kakaocorp.com")
        );
        addUser(user1);
        addUser(user2);
    }
}
