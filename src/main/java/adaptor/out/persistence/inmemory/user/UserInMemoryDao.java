package adaptor.out.persistence.inmemory.user;

import application.out.user.UserDao;
import domain.user.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class UserInMemoryDao implements UserDao {

    private final Map<String, User> users = new HashMap<>();

    @Override
    public void save(User user) {
        users.put(user.getUserId(), user);
    }

    @Override
    public User findByUserId(String userId) {
        return users.get(userId);
    }

    @Override
    public List<User> findAll() {
        return users.values().stream().collect(Collectors.toUnmodifiableList());
    }
}
