package adaptor.out.persistence.user;

import application.out.user.UserDao;
import com.google.common.collect.Maps;
import domain.user.User;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class UserInMemoryDao implements UserDao {

    private final Map<String, User> users = Maps.newHashMap();

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
