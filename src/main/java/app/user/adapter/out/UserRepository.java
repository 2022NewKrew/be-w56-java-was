package app.user.adapter.out;

import app.user.application.port.out.LoadUserPort;
import app.user.application.port.out.SaveUserPort;
import app.user.domain.User;
import com.google.common.collect.Maps;
import java.util.Collection;
import java.util.Map;

public class UserRepository implements SaveUserPort, LoadUserPort {

    private static final Map<String, User> users = Maps.newHashMap();

    public void save(User user) {
        users.put(user.getUserId(), user);
    }

    public User findUserById(String userId) {
        return users.get(userId);
    }

    public Collection<User> findAll() {
        return users.values();
    }
}
