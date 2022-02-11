package model.repository.user;

import model.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserRepositoryJdbc implements UserRepository{
    private static final String DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String URL = "jdbc:mysql://10.202.165.223:3306/kakao?serverTimezone=UTC";
    private static final String USERNAME = "kennypark";
    private static final String PASSWORD = "kennypark";

    @Override
    public User save(User user) {
        if (user.isNew()) {
            int id = insert(user);
            return User.builder()
                    .id(id)
                    .stringId(user.getStringId())
                    .name(user.getName())
                    .password(user.getPassword())
                    .email(user.getEmail())
                    .build();
        }
        update(user);
        return user;
    }

    @Override
    public User findById(int id) {
        try{
            Class.forName(DRIVER);
            Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            String sql = "SELECT ID, STRING_ID, NAME, PASSWORD, EMAIL FROM `USER` WHERE ID = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            rs.next();
            return User.builder()
                    .id(rs.getInt(1))
                    .stringId(rs.getString(2))
                    .name(rs.getString(3))
                    .password(rs.getString(4))
                    .email(rs.getString(5))
                    .build();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            throw new IllegalArgumentException();
        }
    }

    @Override
    public User findByStringId(String stringId) {
        try{
            Class.forName(DRIVER);
            Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            String sql = "SELECT ID, STRING_ID, NAME, PASSWORD, EMAIL FROM `USER` WHERE STRING_ID = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, stringId);
            ResultSet rs = pstmt.executeQuery();
            rs.next();
            return User.builder()
                    .id(rs.getInt(1))
                    .stringId(rs.getString(2))
                    .name(rs.getString(3))
                    .password(rs.getString(4))
                    .email(rs.getString(5))
                    .build();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            throw new IllegalArgumentException();
        }
    }

    @Override
    public List<User> findAll() {
        try{
            Class.forName(DRIVER);
            Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            String sql = "SELECT ID, STRING_ID, NAME, PASSWORD, EMAIL FROM `USER`";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery(sql);
            List<User> users = new ArrayList<>();
            while(rs.next()){
                users.add(User.builder()
                        .id(rs.getInt(1))
                        .stringId(rs.getString(2))
                        .name(rs.getString(3))
                        .password(rs.getString(4))
                        .email(rs.getString(5))
                        .build());
            }
            return users;
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            throw new IllegalArgumentException();
        }
    }

    private int insert(User user){
        try{
            Class.forName(DRIVER);
            Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            String sql = "INSERT INTO `USER`(STRING_ID, NAME, PASSWORD, EMAIL) VALUES (?, ?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, user.getStringId());
            pstmt.setString(2, user.getName());
            pstmt.setString(3, user.getPassword());
            pstmt.setString(4, user.getEmail());
            pstmt.executeUpdate();
            ResultSet rs =  pstmt.getGeneratedKeys();
            rs.next();
            return rs.getInt(1);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            throw new IllegalArgumentException();
        }
    }

    private void update(User user){
        try{
            Class.forName(DRIVER);
            Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            String sql = "UPDATE `USER` SET STRING_ID=?, NAME=?, PASSWORD=?, EMAIL=? WHERE ID=?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, user.getStringId());
            pstmt.setString(2, user.getName());
            pstmt.setString(3, user.getPassword());
            pstmt.setString(4, user.getEmail());
            pstmt.setInt(5, user.getId());
            pstmt.executeUpdate();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            throw new IllegalArgumentException();
        }
    }
}
