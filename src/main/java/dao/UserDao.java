package dao;

import model.User;

import java.sql.SQLException;
import java.util.List;

public interface UserDao {
    public void save(User user) throws SQLException;
    public User findByUserId(final String userId) throws SQLException;
    public List<User> findAll() throws SQLException;
    public void delete(final String userId) throws SQLException;
}
