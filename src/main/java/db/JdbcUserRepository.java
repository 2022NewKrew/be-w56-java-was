package db;

import domain.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JdbcUserRepository {

    private static final Logger log = LoggerFactory.getLogger(JdbcUserRepository.class);

    public static void addUser(User user) {
        try(Connection connection = DataBaseConnection.connection();) {
            String insertSql = "insert into user_account (user_id, password, email, name) values (?, ?, ?, ?)";
            PreparedStatement insertStatement = connection.prepareStatement(insertSql);
            insertStatement.setString(1, user.getUserId());
            insertStatement.setString(2, user.getPassword());
            insertStatement.setString(3, user.getEmail());
            insertStatement.setString(4, user.getName());
            insertStatement.executeUpdate();

        } catch (SQLException sqlException) {
            log.error(sqlException.getMessage(), sqlException);
        }

    }

    public static Optional<User> findUserById(String userId) {
        try(Connection connection = DataBaseConnection.connection()) {
            String sql = "select user_id, password, email, name from user_account where user_id = ?";
            PreparedStatement selectStatement = connection.prepareStatement(sql);
            selectStatement.setString(1, userId);

            ResultSet resultSet = selectStatement.executeQuery();
            if(resultSet.next()) {
                User result = new User(resultSet.getString("user_id"),
                        resultSet.getString("password"),
                        resultSet.getString("name"),
                        resultSet.getString("email"));
                return Optional.of(result);
            }
        } catch (SQLException sqlException) {
            log.error(sqlException.getMessage(), sqlException);
        }

        return Optional.empty();
    }

    public static List<User> findAll() {
        List<User> users = new ArrayList<>();
        try(Connection connection = DataBaseConnection.connection()) {
            String sql = "select user_id, password, email, name from user_account";
            PreparedStatement selectStatement = connection.prepareStatement(sql);

            ResultSet resultSet = selectStatement.executeQuery();
            while(resultSet.next()) {
                User result = new User(resultSet.getString("user_id"),
                        resultSet.getString("password"),
                        resultSet.getString("name"),
                        resultSet.getString("email"));
                users.add(result);
            }
            return users;
        } catch (SQLException sqlException) {
            log.error(sqlException.getMessage(), sqlException);
        }
        return users;
    }

}
