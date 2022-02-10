package dao;

import CustomException.DuplicateKeyException;
import CustomException.MysqlErrorCode;
import dao.connection.ConnectionMaker;
import model.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDaoImpl implements UserDao {
    private ConnectionMaker connectionMaker;
    private Connection connection;

    public UserDaoImpl(ConnectionMaker connectionMaker) {
        this.connectionMaker = connectionMaker;
        this.connection = connectionMaker.getConnection();
    }

    @Override
    public void save(User user) {
        String sql = String.format("insert into %s (%s, %s, %s, %s) values (?,?,?,?)",
                UserDBConstants.TABLE_NAME,
                UserDBConstants.COLUMN_USER_ID,
                UserDBConstants.COLUMN_PASSWORD,
                UserDBConstants.COLUMN_NAME,
                UserDBConstants.COLUMN_EMAIL);

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(UserDBConstants.COLUMN_INDEX_USER_ID, user.getUserId());
            preparedStatement.setString(UserDBConstants.COLUMN_INDEX_PASSWORD, user.getPassword());
            preparedStatement.setString(UserDBConstants.COLUMN_INDEX_EMAIL, user.getName());
            preparedStatement.setString(UserDBConstants.COLUMN_INDEX_NAME, user.getEmail());

            int count = preparedStatement.executeUpdate();

            if (count == 0) {
                throw new SQLException("SQL Insertion failed");
            }

            preparedStatement.close();
        } catch (SQLException e) {
            if (e.getErrorCode() == MysqlErrorCode.DUPLICATE_KEY) {
                throw new DuplicateKeyException(e);
            }
            throw new RuntimeException(String.format("SQL 오류, error code = %d", e.getErrorCode()));
        }
    }

    @Override
    public User findByUserId(final String userId) {
        String sql = String.format("SELECT * from %s where %s = \"%s\"",
                UserDBConstants.TABLE_NAME,
                UserDBConstants.COLUMN_USER_ID,
                userId);

        try {
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(sql);

            if (rs.next()) {
                return new User(
                        rs.getString(UserDBConstants.COLUMN_USER_ID),
                        rs.getString(UserDBConstants.COLUMN_PASSWORD),
                        rs.getString(UserDBConstants.COLUMN_NAME),
                        rs.getString(UserDBConstants.COLUMN_EMAIL)
                );
            } else {
                return null;
            }
        } catch (SQLException e) {
            throw new RuntimeException(String.format("SQL 오류, error code = %d", e.getErrorCode()));
        }
    }

    @Override
    public List<User> findAll() {
        List<User> userList = new ArrayList<>();
        String sql = String.format("Select * from %s", UserDBConstants.TABLE_NAME);

        try {
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(sql);

            while (rs.next()) {
                userList.add(new User(
                        rs.getString(UserDBConstants.COLUMN_USER_ID),
                        rs.getString(UserDBConstants.COLUMN_PASSWORD),
                        rs.getString(UserDBConstants.COLUMN_NAME),
                        rs.getString(UserDBConstants.COLUMN_EMAIL)
                ));
            }
        } catch (SQLException e) {
            throw new RuntimeException(String.format("SQL 오류, error code = %d", e.getErrorCode()));
        }

        return userList;
    }

    @Override
    public void delete(final String userId) {
        String sql = String.format("DELETE FROM %s WHERE %s = \"%s\"",
                UserDBConstants.TABLE_NAME,
                UserDBConstants.COLUMN_USER_ID,
                userId);

        try {
            Statement statement = connection.createStatement();
            statement.executeUpdate(sql);
        } catch (SQLException e) {
            throw new RuntimeException(String.format("SQL 오류, error code = %d", e.getErrorCode()));
        }
    }
}
