package application.repository;

import static application.repository.DbConstants.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import com.zaxxer.hikari.HikariDataSource;

import application.model.User;


public class DbUserRepository implements UserRepository {

    public static DbUserRepository getInstance() {
        return LazyHolder.instance;
    }

    private final HikariDataSource dataSource;

    private DbUserRepository() {
        dataSource = new HikariDataSource();
        dataSource.setJdbcUrl(URL);
        dataSource.setUsername(USER);
        dataSource.setPassword(PASSWORD);
        dataSource.setMaximumPoolSize(10);
    }

    @Override
    public void addUser(User user) {
        String SQL = "INSERT INTO USER(userid, password, name, email) VALUES (?, ?, ?, ?);";
        String[] values = { user.getUserId(), user.getPassword(), user.getName(), user.getEmail() };
        Object result = query(SQL, values, (value) -> value);
        return;
    }

    @Override
    public User findUserById(String userId) {
        String SQL = "SELECT userid, password, name, email FROM USER WHERE userid = ?";
        String[] values = { userId };
        Object result = query(SQL, values, this::convertResultSetToUser);
        return (User) result;
    }

    @Override
    public List<User> findAll() {
        String SQL = "SELECT userid, password, name, email FROM USER";
        String[] values = {};
        Object result = query(SQL, values, this::convertResultSetToUsers);
        return (List<User>) result;
    }

    private List<User> convertResultSetToUsers(ResultSet resultSet) {
        try {
            return getUsersFromResultSet(resultSet);
        } catch (SQLException e) {
            e.printStackTrace();
            return new ArrayList<User>();
        }
    }

    private User convertResultSetToUser(ResultSet resultSet) {
        try {
            List<User> findUsers = getUsersFromResultSet(resultSet);
            return (findUsers.size() > 0) ? findUsers.get(0) : null;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    private List<User> getUsersFromResultSet(ResultSet resultSet) throws SQLException {
        ArrayList<User> users = new ArrayList<>();
        while (resultSet.next()) {
            User findUser = new User(resultSet.getString("userid"), resultSet.getNString("password"),
                                     resultSet.getString("name"), resultSet.getString("email"));
            users.add(findUser);
        }
        return users;
    }

    private Object query(String SQL, String[] values, Function<ResultSet, Object> convertor) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet resultSet = null;

        try {
            conn = dataSource.getConnection();
            pstmt = makePreparedStatement(conn, SQL, values);
            resultSet = executeAndGetResult(pstmt);

            return convertor.apply(resultSet);

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        } finally {
            closeConnection(conn, pstmt, resultSet);
        }
    }

    private PreparedStatement makePreparedStatement(Connection conn, String SQL, String[] values) throws SQLException {
        PreparedStatement pstmt = conn.prepareStatement(SQL);
        for (int i = 0; i < values.length; i++) {
            pstmt.setString(i + 1, values[i]);
        }
        return pstmt;
    }

    private ResultSet executeAndGetResult(PreparedStatement pstmt) throws SQLException {
        boolean hasResultSet = pstmt.execute();
        return (hasResultSet) ? pstmt.getResultSet() : null;
    }

    private void closeConnection(Connection conn, PreparedStatement pstmt, ResultSet rs) {
        if (pstmt != null) {
            try {
                pstmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private static class LazyHolder {
        private static final DbUserRepository instance = new DbUserRepository();
    }
}
