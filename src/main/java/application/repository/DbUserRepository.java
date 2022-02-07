package application.repository;

import static application.repository.DbConstants.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import application.model.User;

public class DbUserRepository implements UserRepository {

    private Connection conn = null;
    private PreparedStatement pstmt = null;
    private ResultSet resultSet = null;

    public DbUserRepository() {
        try {
            Class.forName(DRIVER);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void addUser(User user) {
        try {
            String SQL = "INSERT INTO USER(userid, password, name, email) VALUES (?, ?, ?, ?);";
            String[] values = { user.getUserId(), user.getPassword(), user.getName(), user.getEmail() };
            query(SQL, values);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection(conn, pstmt, resultSet);
        }
    }

    @Override
    public User findUserById(String userId) {
        try {
            String SQL = "SELECT userid, password, name, email FROM USER WHERE userid = ?";
            String[] values = { userId };
            resultSet = query(SQL, values);
            List<User> findUsers = getUsersFromResultSet(resultSet);
            return (findUsers.size() > 0) ? findUsers.get(0) : null;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection(conn, pstmt, resultSet);
        }
        return null;
    }

    @Override
    public List<User> findAll() {
        try {
            String SQL = "SELECT userid, password, name, email FROM USER";
            String[] values = {};
            resultSet = query(SQL, values);
            List<User> findUsers = getUsersFromResultSet(resultSet);
            return findUsers;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
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

    private synchronized ResultSet query(String SQL, String[] values) throws SQLException {
        conn = DriverManager.getConnection(URL, USER, PASSWORD);
        pstmt = conn.prepareStatement(SQL);
        setParams(pstmt, values);
        boolean hasResultSet = pstmt.execute();
        return (hasResultSet) ? pstmt.getResultSet() : null;
    }


    private void setParams(PreparedStatement pstmt, String[] values) throws SQLException {
        for (int i = 0; i < values.length; i++) {
            pstmt.setString(i + 1, values[i]);
        }
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
}
