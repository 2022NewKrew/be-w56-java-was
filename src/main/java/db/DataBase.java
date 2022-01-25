package db;

import java.util.Collection;
import java.util.Map;

import com.google.common.collect.Maps;

import model.User;

public class DataBase {
    private static Map<String, User> users = Maps.newHashMap();

    public static DataBase getInstance(){
        return LazyHolder.instance;
    }
    synchronized public void addUser(User user) {
        users.put(user.getUserId(), user);
    }

    synchronized public User findUserById(String userId) {
        return users.get(userId);
    }

    synchronized public Collection<User> findAll() {
        return users.values();
    }
    private static class LazyHolder{
        private static final DataBase instance = new DataBase();
    }
}
