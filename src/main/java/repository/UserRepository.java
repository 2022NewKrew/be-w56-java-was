package repository;

import com.google.common.collect.Maps;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import model.User;

public class UserRepository {

    private static final Map<String, User> users = Maps.newConcurrentMap();

    static {
        users.put("woojin7124",
            User.builder().userId("woojin7124").password("1234").email("woojin7124@naver.com")
                .name("WOOJIN JANG").build());
        users.put("woojin7125",
            User.builder().userId("woojin7125").password("1234").email("woojin7125@naver.com")
                .name("WOOJIN JANG").build());
    }

    public void save(User user) {
        users.put(user.getUserId(), user);
    }

    public Optional<User> findById(String userId) {
        return Optional.ofNullable(users.get(userId));
    }

    public List<User> findAll() {
        return new ArrayList<>(users.values());
    }
}
