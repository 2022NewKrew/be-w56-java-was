package db;

import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
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
        return Optional.empty();
    }

    @Override
    public List<User> findAll() {
        return null;
    }
}
