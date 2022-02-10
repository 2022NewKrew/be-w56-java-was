package adaptor.out.persistence.mysql.user;

import adaptor.out.persistence.mysql.QueryBuilder;
import application.out.user.UserDao;
import domain.user.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static infrastructure.config.DatabaseConfig.*;

public class UserMysqlDao implements UserDao {

    private static final Logger log = LoggerFactory.getLogger(UserMysqlDao.class);
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_PASSWORD = "password";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_EMAIL = "email";
    private final QueryBuilder queryBuilder;

    public UserMysqlDao(QueryBuilder queryBuilder) {
        this.queryBuilder = queryBuilder;
    }

    @Override
    public void save(User user) {
        try (Connection conn = DriverManager.getConnection(DB_URL, USER_NAME, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(queryBuilder.insertOne(COLUMN_ID, COLUMN_PASSWORD, COLUMN_NAME, COLUMN_EMAIL))
        ) {
            pstmt.setString(1, user.getUserId());
            pstmt.setString(2, user.getPassword());
            pstmt.setString(3, user.getName());
            pstmt.setString(4, user.getEmail());

            pstmt.executeUpdate();
        } catch (SQLException e) {
            log.debug(e.getMessage());
        }
    }

    @Override
    public User findByUserId(String userId) {
        try (Connection conn = DriverManager.getConnection(DB_URL, USER_NAME, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(queryBuilder.selectOne(COLUMN_ID))
        ) {
            pstmt.setString(1, userId);
            ResultSet resultSet = pstmt.executeQuery();

            while (resultSet.next()) {
                return User.builder()
                        .userId(resultSet.getString(COLUMN_ID))
                        .password(resultSet.getString(COLUMN_PASSWORD))
                        .name(resultSet.getString(COLUMN_NAME))
                        .email(resultSet.getString(COLUMN_EMAIL))
                        .build();
            }
        } catch (SQLException e) {
            log.debug(e.getMessage());
        }
        return null;
    }

    @Override
    public List<User> findAll() {
        List<User> users = new ArrayList<>();

        try (Connection conn = DriverManager.getConnection(DB_URL, USER_NAME, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(queryBuilder.selectAll())
        ) {
            ResultSet resultSet = pstmt.executeQuery();

            while (resultSet.next()) {
                users.add(User.builder()
                        .userId(resultSet.getString(COLUMN_ID))
                        .password(resultSet.getString(COLUMN_PASSWORD))
                        .name(resultSet.getString(COLUMN_NAME))
                        .email(resultSet.getString(COLUMN_EMAIL))
                        .build());
            }
        } catch (SQLException e) {
            log.debug(e.getMessage());
        }
        return users;
    }
}
