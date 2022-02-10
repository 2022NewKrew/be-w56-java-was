package repo;

import jdbc.JedisPools;
import model.User;
import org.json.JSONObject;
import redis.clients.jedis.Jedis;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class UserJdbc {
    private static final String KEY_PREFIX = "USER::";
    private static final String KEY_LIST = "USER";

    private final JedisPools jedisPools;

    public UserJdbc(final JedisPools jedisPools) {
        this.jedisPools = jedisPools;
    }

    public void addUser(final User user) {
        final Jedis jedis = jedisPools.get();
        final String id = user.getId();

        jedis.rpush(KEY_LIST, id);

        final JSONObject jsonUser = new JSONObject();
        jsonUser.put(User.KEY_ID, id);
        jsonUser.put(User.KEY_PASSWORD, user.getPassword());
        jsonUser.put(User.KEY_NAME, user.getName());
        jsonUser.put(User.KEY_EMAIL, user.getEmail());
        jedis.set(KEY_PREFIX + id, jsonUser.toString());

        jedisPools.release(jedis);
    }

    public User findUserById(final String id) {
        final Jedis jedis = jedisPools.get();
        final String strUser = jedis.get(KEY_PREFIX + id);
        jedisPools.release(jedis);

        if (strUser == null) {
            return null;
        }
        return new User(new JSONObject(strUser));
    }

    public List<User> findAll() {
        final Jedis jedis = jedisPools.get();
        final List<String> ids = jedis.lrange(KEY_LIST, 0, -1);
        final List<User> users = new ArrayList<>();
        for (String id : ids) {
            users.add(new User(new JSONObject(jedis.get(KEY_PREFIX + id))));
        }
        jedisPools.release(jedis);

        return Collections.unmodifiableList(users);
    }
}
