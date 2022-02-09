package repository;

import config.JdbcConfig;
import model.User;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public class InMemoryUserRepository {

    private static InMemoryUserRepository instance;

    private final Connection connection;

    public static InMemoryUserRepository getInstance() {
        if (instance == null) {
            instance = new InMemoryUserRepository(JdbcConfig.getConnection());
            instance.createTable();
        }
        return instance;
    }

    private InMemoryUserRepository(Connection connection) {
        this.connection = connection;
    }

    private void createTable() {
        String sql = "CREATE TABLE `USER`("
                + "user_id VARCHAR(15) NOT NULL PRIMARY KEY,"
                + "password VARCHAR(15) NOT NULL,"
                + "name VARCHAR(10) NOT NULL,"
                + "email VARCHAR(15) NOT NULL"
                + ");";

        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addUser(User user) {
        String sql = "INSERT INTO `USER` (user_id, password, name, email)"
                + "VALUES ("
                + "'" + user.getUserId() + "',"
                + "'" + user.getPassword() + "',"
                + "'" + user.getName() + "',"
                + "'" + user.getEmail() + "'"
                + ");";

        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Optional<User> findUserById(String userId) {
        String sql = "SELECT * FROM `USER` WHERE USER_ID = '" + userId + "';";

        try (Statement statement = connection.createStatement()) {
            ResultSet rs = statement.executeQuery(sql);
            return Optional.ofNullable(resultMappingUser(rs));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    private User resultMappingUser(ResultSet rs) throws SQLException {
        if (!rs.next()) {
            return null;
        }
        return new User(rs.getString("user_id"),
                rs.getString("password"),
                rs.getString("name"),
                rs.getString("email"));
    }

    public Collection<User> findAll() {
        String sql = "SELECT * FROM `USER`";

        try (Statement statement = connection.createStatement()) {
            ResultSet rs = statement.executeQuery(sql);
            return resultMappingUsers(rs);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return List.of();
    }

    private List<User> resultMappingUsers(ResultSet rs) throws SQLException {
        List<User> users = new ArrayList<>();
        User tmpUser;
        while ((tmpUser = resultMappingUser(rs)) != null) {
            users.add(tmpUser);
        }
        return users;
    }


}
