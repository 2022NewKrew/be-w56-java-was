package application.repository;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.google.common.collect.Maps;

import application.model.User;

public class DataBase {
    private static ConcurrentHashMap<String, User> users = new ConcurrentHashMap<>();

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
