package application.domain;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class UserRepository {
    private final Map<String, User> users = new HashMap<>();

    public void save(User user) {
        users.put(user.getName(), user);
    }

    public Optional<User> findById(String name) {
        return Optional.ofNullable(users.get(name));
    }
}
