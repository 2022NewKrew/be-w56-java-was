package repository;

import java.util.Collection;
import java.util.Map;

import com.google.common.collect.Maps;

import lombok.extern.slf4j.Slf4j;
import model.User;

@Slf4j
public class UserRepository {
    private static final UserRepository INSTANCE = new UserRepository();

    public static UserRepository getInstance() {
        return INSTANCE;
    }

    private UserRepository() {
    }

    private Map<String, User> users = Maps.newHashMap();

    public void save(User user) {

        users.put(user.getUserId(), user);
    }

    public User findById(String userId) {
        return users.get(userId);
    }

    public Collection<User> findAll() {
        return users.values();
    }
}
