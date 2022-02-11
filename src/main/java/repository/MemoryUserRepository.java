package repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import model.User;

public class MemoryUserRepository implements UserRepository {
    private final Map<String, User> users = new HashMap<>();

    @Override
    public void save(User user) {
        users.put(user.getUserId(), user);
    }

    @Override
    public Optional<User> findUserById(String userId) {
        return Optional.ofNullable(users.get(userId));
    }

    @Override
    public List<User> findAll() {
        return users.values().stream().collect(Collectors.toCollection(ArrayList::new));
    }
}
