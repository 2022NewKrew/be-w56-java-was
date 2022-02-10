package dao;

import model.User;

import java.sql.SQLException;
import java.util.List;

public interface UserDao {
    public void save(User user);
    public User findByUserId(final String userId);
    public List<User> findAll();
    public void delete(final String userId);
}
