package application.domain;

import di.annotation.Bean;

import java.util.*;
import java.util.stream.Collectors;

@Bean
public class UserRepository {
    private final Map<String, User> users = new HashMap<>();

    public void save(User user) {
        users.put(user.getName(), user);
    }

    public Optional<User> findById(String name) {
        return Optional.ofNullable(users.get(name));
    }

    public List<User> findAll() {
        return new ArrayList<>(users.values());
    }
}
