package domain.user.repository;

import domain.user.model.User;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class InMemoryUserRepository implements UserRepository {

    private static InMemoryUserRepository instance;

    private final Map<String, User> users;

    public static InMemoryUserRepository get() {
        if (instance == null) {
            instance = new InMemoryUserRepository(new HashMap<>());
        }
        return instance;
    }

    private InMemoryUserRepository(Map<String, User> users) {
        this.users = users;
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
