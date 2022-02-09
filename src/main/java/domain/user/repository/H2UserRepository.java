package domain.user.repository;

import domain.user.model.User;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public class H2UserRepository implements UserRepository {

    private static H2UserRepository instance;

    private final Connection connection;

    public static H2UserRepository get() {
        if (instance == null) {
            instance = new H2UserRepository(getConnection());
            instance.createTable();
        }
        return instance;
    }

    private H2UserRepository(Connection connection) {
        this.connection = connection;
    }

    private static Connection getConnection() {
        try {
            return DriverManager.getConnection("jdbc:h2:~/mem", "sa", "sa");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    private void createTable() {
        String dropSql = "DROP TABLE IF EXISTS CAFE_USER";
        String sql = "CREATE TABLE CAFE_USER("
            + "user_id VARCHAR(15) NOT NULL PRIMARY KEY,"
            + "password VARCHAR(15) NOT NULL,"
            + "name VARCHAR(10) NOT NULL,"
            + "email VARCHAR(15) NOT NULL"
            + ");";

        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(dropSql);
            statement.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void save(User user) {
        String sql = "INSERT INTO CAFE_USER (user_id, password, name, email)"
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

    @Override
    public Optional<User> findUserByUserId(String userId) {
        String sql =
            "SELECT * FROM CAFE_USER WHERE USER_ID = '" + userId + "';";

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
        return User.builder()
            .userId(rs.getString("user_id"))
            .password(rs.getString("password"))
            .name(rs.getString("name"))
            .email(rs.getString("email"))
            .build();
    }

    @Override
    public Collection<User> findAll() {
        String sql = "SELECT * FROM CAFE_USER";

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
