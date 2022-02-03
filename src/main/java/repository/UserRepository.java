package repository;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;

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

    public void update(User user) {
        if (users.containsKey(user.getUserId())) {
            users.remove(user.getUserId());
        }
        users.put(user.getUserId(), user);
    }

    public Optional<User> findById(String userId) {
        User entity = users.get(userId);
        return Optional.ofNullable(entity);
    }

    public Collection<User> findAll() {
        return users.values();
    }
}
