package webserver.domain.repository;

import webserver.domain.entity.User;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class UserRepositoryImpl implements UserRepository{
    private final ConcurrentHashMap<String, User> idToUser = new ConcurrentHashMap<>();

    @Override
    public Optional<User> getUser(String id) {
        return Optional.ofNullable(idToUser.get(id));
    }

    @Override
    public void saveUser(User user) {
        idToUser.put(user.getUserId(), user);
    }
}
