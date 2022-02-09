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
    private final QueryBuilder queryBuilder;

    public UserMysqlDao(QueryBuilder queryBuilder) {
        this.queryBuilder = queryBuilder;
    }

    @Override
    public void save(User user) {
        try (Connection conn = DriverManager.getConnection(DB_URL, USER_NAME, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(queryBuilder.insertOne("id", "password", "name", "email"))
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
             PreparedStatement pstmt = conn.prepareStatement(queryBuilder.selectOne("id"))
        ) {
            pstmt.setString(1, userId);
            ResultSet resultSet = pstmt.executeQuery();

            while (resultSet.next()) {
                return User.builder()
                        .userId(resultSet.getString("id"))
                        .password(resultSet.getString("password"))
                        .name(resultSet.getString("name"))
                        .email(resultSet.getString("email"))
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
                        .userId(resultSet.getString("id"))
                        .password(resultSet.getString("password"))
                        .name(resultSet.getString("name"))
                        .email(resultSet.getString("email"))
                        .build());
            }
        } catch (SQLException e) {
            log.debug(e.getMessage());
        }
        return users;
    }
}
