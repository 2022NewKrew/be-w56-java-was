package repository;

import db.JdbcConnection;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.*;

public class UserRepository implements Repository<User>{

    private static final Logger log = LoggerFactory.getLogger(UserRepository.class);

    @Override
    public User create(User user) {
        String sql = "INSERT INTO USER_WAS (USER_ID, NAME, EMAIL, PASSWORD) VALUES (?, ?, ?, ?)";
        try {
            Connection conn = JdbcConnection.getConnection();
            PreparedStatement psmt = conn.prepareStatement(sql);
            psmt.setString(1, user.getUserId());
            psmt.setString(2, user.getName());
            psmt.setString(3, user.getEmail());
            psmt.setString(4, user.getPassword());
            psmt.execute();

            conn.close();
            psmt.close();
        } catch (SQLException e) {
            log.error("could not create user : {}", e.getMessage());
            e.printStackTrace();
        }
        return user;
    }

    @Override
    public Collection<User> findAll() {
        String sql = "SELECT USER_ID, NAME, EMAIL, PASSWORD FROM USER_WAS";
        try {
            Connection conn = JdbcConnection.getConnection();
            PreparedStatement psmt = conn.prepareStatement(sql);
            ResultSet rs = psmt.executeQuery();
            List<User> result = new ArrayList<>();
            if (rs.next()){
                User user = userMaker(rs);
                result.add(user);
            }
            conn.close();
            psmt.close();
            return result;

        } catch (SQLException e) {
            log.error("could not find userList : {}", e.getMessage());
            e.printStackTrace();
        }
        return Collections.emptyList();
    }

    @Override
    public Optional<User> findById(String id) {
        String sql = "SELECT USER_ID, NAME, EMAIL, PASSWORD FROM USER_WAS WHERE USER_ID = ?";
        try {
            Connection conn = JdbcConnection.getConnection();
            PreparedStatement psmt = conn.prepareStatement(sql);
            psmt.setString(1, id);
            ResultSet rs = psmt.executeQuery();

            User user = null;
            if (rs.next())
                user = userMaker(rs);

            conn.close();
            psmt.close();
            return Optional.of(user);

        } catch (SQLException e) {
            log.error("could not find user : {}", e.getMessage());
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public User update(User user) {
        String sql = "UPDATE USER_WAS SET NAME = ?, EMAIL = ?, PASSWORD = ? WHERE USER_ID = ?,";
        try {
            Connection conn = JdbcConnection.getConnection();
            PreparedStatement psmt = conn.prepareStatement(sql);
            psmt.setString(1, user.getName());
            psmt.setString(2, user.getEmail());
            psmt.setString(3, user.getPassword());
            psmt.setString(4, user.getUserId());
            psmt.execute();

            conn.close();
            psmt.close();

        } catch (SQLException e) {
            log.error("could not find userList : {}", e.getMessage());
            e.printStackTrace();
        }
        return user;
    }

    @Override
    public void delete(User user) {
        String sql = "DELETE FROM USER_WAS WHERE USER_ID = ?";
        try{
            Connection conn = JdbcConnection.getConnection();
            PreparedStatement psmt = conn.prepareStatement(sql);
            psmt.setString(1, user.getUserId());
            psmt.execute();

            conn.close();
            psmt.close();

        } catch (SQLException e) {
            log.error("could not delete user : {}", e.getMessage());
            e.printStackTrace();
        }
    }

    private User userMaker(ResultSet rs) throws SQLException {
        return new User(
                rs.getString("USER_ID"),
                rs.getString("NAME"),
                rs.getString("EMAIL"),
                rs.getString("PASSWORD")
        );
    }
}
