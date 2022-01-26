package domain.user.repository;

import domain.user.model.User;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class InMemoryUserRepository implements UserRepository {

    private final Map<String, User> users;

    public InMemoryUserRepository() {
        this.users = new HashMap<>();
    }

    @Override
    public void save(User user) {
        users.put(user.getUserId(), user);
    }

    @Override
    public Optional<User> findUserByUserId(String userId) {
        return Optional.ofNullable(users.get(userId));
    }

    @Override
    public Collection<User> findAll() {
        return users.values();
    }
}
