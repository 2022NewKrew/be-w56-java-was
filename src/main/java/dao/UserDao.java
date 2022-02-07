package dao;

import model.User;

import java.sql.SQLException;
import java.util.List;
import java.util.NoSuchElementException;

public interface UserDao {
    public void openConnection() throws SQLException, ClassNotFoundException;
    public void closeConnection() throws SQLException;
    public void save(User user) throws SQLException;
    public User findByUserId(final String userId) throws SQLException;
    public List<User> findAll() throws SQLException;
}
