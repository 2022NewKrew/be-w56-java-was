package repository;

import com.google.common.collect.Maps;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import model.User;

public class UserRepository {

    private static final Map<String, User> users = Maps.newConcurrentMap();

    public void save(User user) {
        users.put(user.getUserId(), user);
    }

    public Optional<User> findById(String userId) {
        return Optional.ofNullable(users.get(userId));
    }

    public List<User> findAll() {
        return new ArrayList<>(users.values());
    }
}
