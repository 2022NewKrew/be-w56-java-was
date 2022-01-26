package adaptor.out.persistence.user;

import application.out.UserDao;
import com.google.common.collect.Maps;
import domain.user.User;

import java.util.Map;

public class UserInMemoryDao implements UserDao {
    private static final UserInMemoryDao INSTANCE = new UserInMemoryDao();
    private final static Map<String, User> users = Maps.newHashMap();

    private UserInMemoryDao() {
    }

    public static UserInMemoryDao getINSTANCE() {
        return INSTANCE;
    }

    @Override
    public void save(User user) {
        users.put(user.getUserId(), user);
    }
}
