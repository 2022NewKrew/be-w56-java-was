package db;

import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserRepositoryImpl implements UserRepository{
    private static final Logger log = LoggerFactory.getLogger(UserRepositoryImpl.class);
    private static final UserRepositoryImpl userRepositoryImpl = new UserRepositoryImpl();
    private final DBConnection dbConnection = DBConnection.getInstance();

    private UserRepositoryImpl(){}

    public static UserRepositoryImpl getInstance(){
        return userRepositoryImpl;
    }


    @Override
    public void addUser(User user) {
        PreparedStatement pstmt = null;
        Connection con = null;
        try {
            con = dbConnection.getConnection();
            String sql = "INSERT INTO users (userId, password, name, email) VALUES (?,?,?,?)";
            pstmt = con.prepareStatement(sql);

            pstmt.setString(1, user.getUserId());
            pstmt.setString(2, user.getPassword());
            pstmt.setString(3, user.getName());
            pstmt.setString(4, user.getEmail());

            pstmt.executeUpdate();
            log.info("[UserRepositoryImpl]: add User Complete");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            dbConnection.close(pstmt, con);
        }
    }

    @Override
    public Optional<User> findUserById(String userId) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        User user = null;
        try{
            con = dbConnection.getConnection();
            String sql = "SELECT userId, password, name, email FROM users WHERE userId = ?";
            pstmt = con.prepareStatement(sql);

            pstmt.setString(1, userId);

            rs = pstmt.executeQuery();

            if(rs.next()){
                user = new User(
                        rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4)
                );
            }

            log.info("[UserRepositoryImpl]: search User by UserId Complete");
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            dbConnection.close(rs, pstmt, con);
        }
        return Optional.ofNullable(user);
    }

    @Override
    public List<User> findAll() {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<User> users = new ArrayList<>();

        try {
            con = dbConnection.getConnection();
            String sql = "SELECT userId, password, name, email FROM users";
            pstmt = con.prepareStatement(sql);

            rs = pstmt.executeQuery();

            while(rs.next()){
                users.add(
                        new User(
                                rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4)
                        )
                );
            }

            log.info("[UserRepositoryImpl]: search All User Complete");
        } catch (SQLException e){
            e.printStackTrace();
        } finally {
            dbConnection.close(rs, pstmt, con);
        }

        return users;
    }
}
