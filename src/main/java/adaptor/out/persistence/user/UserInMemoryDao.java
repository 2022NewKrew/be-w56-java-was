package adaptor.out.persistence.user;

import application.out.user.UserDao;
import com.google.common.collect.Maps;
import domain.user.User;

import java.util.Map;

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
}
