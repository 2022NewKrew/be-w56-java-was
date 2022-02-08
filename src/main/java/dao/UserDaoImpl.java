package dao;

import model.User;
import model.UserDBConstants;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDaoImpl implements UserDao {
    private Connection connection;

    public void openConnection() throws SQLException, ClassNotFoundException {
        if (connection != null)
            return;

        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection(
                    "jdbc:mysql://test-cafe-ed.ay1.krane.9rum.cc:3306/javawas",
                    "ed3",
                    "Ed1q2w3e4r!"
            );
        } catch (SQLException e) {
            throw new SQLException("failed to get connection");
        } catch (ClassNotFoundException e) {
            throw new ClassNotFoundException("failed to load driver");
        }

    }

    public void closeConnection() throws SQLException {
        try {
            if (connection != null && connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            throw new SQLException("failed to close connection");
        }
    }

    public void save(User user) throws SQLException {
        String sql = String.format("insert into %s (%s, %s, %s, %s) values (?,?,?,?)",
                UserDBConstants.TABLE_NAME,
                UserDBConstants.COLUMN_USER_ID,
                UserDBConstants.COLUMN_PASSWORD,
                UserDBConstants.COLUMN_NAME,
                UserDBConstants.COLUMN_EMAIL);

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
    }

    public User findByUserId(final String userId) throws SQLException {
        String sql = String.format("SELECT * from %s where %s = \"%s\"",
                UserDBConstants.TABLE_NAME,
                UserDBConstants.COLUMN_USER_ID,
                userId);

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
    }

    public List<User> findAll() throws SQLException {
        List<User> userList = new ArrayList<>();
        String sql = String.format("Select * from %s", UserDBConstants.TABLE_NAME);

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

        return userList;
    }

    public void delete(final String userId) throws SQLException {
        String sql = String.format("DELETE FROM %s WHERE %s = \"%s\"",
                UserDBConstants.TABLE_NAME,
                UserDBConstants.COLUMN_USER_ID,
                userId);

        Statement statement = connection.createStatement();
        statement.executeUpdate(sql);
    }
}
