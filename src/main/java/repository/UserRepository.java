package repository;

import db.DatabaseConnector;
import mapper.RowMapper;
import mapper.UserRowMapper;
import model.User;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class UserRepository implements RepositoryInterface<User> {
    private final DatabaseConnector databaseConnector;
    private final RowMapper<User> rowMapper;

    private UserRepository() {
        databaseConnector = DatabaseConnector.getInstance();
        rowMapper = new UserRowMapper();
    }

    private static class UserRepositoryHelper {
        private static final UserRepository INSTANCE = new UserRepository();
    }

    public static UserRepository getInstance() {
        return UserRepositoryHelper.INSTANCE;
    }

    @Override
    public void join(User target) throws SQLException {
        String sql = "insert into user values(?, ?, ?, ?)";
        databaseConnector.update(sql, target.getUserId(), target.getPassword(), target.getName(), target.getEmail());
    }

    @Override
    public void update(User target) throws SQLException {

    }

    @Override
    public Optional<User> findOne(String index) throws SQLException {
        String sql = "select * from user where userId = ?";
        return databaseConnector.query(sql, rowMapper, index);
    }

    @Override
    public List<User> findAll() throws SQLException {
        String sql = "select * from user";
        return databaseConnector.queryAll(sql, rowMapper);
    }
}
