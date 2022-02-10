package webserver.domain.repository;

import webserver.domain.entity.User;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class UserRepositoryImpl implements UserRepository{
    private final ConcurrentHashMap<String, User> idToUser = new ConcurrentHashMap<>();
    private final List<User> userList = Collections.synchronizedList(new ArrayList<>());

    @Override
    public List<User> getAll() {
        return Collections.unmodifiableList(userList);
    }

    @Override
    public Optional<User> getBy(String id) {
        return Optional.ofNullable(idToUser.get(id));
    }

    @Override
    public void save(User user) {
        idToUser.put(user.getUserId(), user);
        userList.add(user);
    }

    @Override
    public void saveAll(List<User> users) {

    }

    @Override
    public void delete(String userId) {

    }

    @Override
    public void deleteAll() {

    }
}
