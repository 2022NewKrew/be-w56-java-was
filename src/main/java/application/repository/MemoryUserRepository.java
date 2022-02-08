package application.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import application.model.User;

public class MemoryUserRepository implements UserRepository {
    private static ConcurrentHashMap<String, User> users = new ConcurrentHashMap<>();

    public static MemoryUserRepository getInstance(){
        return LazyHolder.instance;
    }
    synchronized public void addUser(User user) {
        users.put(user.getUserId(), user);
    }

    synchronized public User findUserById(String userId) {
        return users.get(userId);
    }

    synchronized public List<User> findAll() {
        return new ArrayList<>(users.values());
    }
    private static class LazyHolder{
        private static final MemoryUserRepository instance = new MemoryUserRepository();
    }
}
