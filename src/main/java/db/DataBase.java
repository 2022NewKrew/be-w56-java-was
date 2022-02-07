package db;

import java.util.Collection;
import java.util.Map;
import java.sql.*;

import com.google.common.collect.Maps;

import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.DataBaseUtils;

public class DataBase {

    private static Map<String, User> users = Maps.newHashMap();
    private static Logger log = LoggerFactory.getLogger(DataBase.class);

    public static void addUser(User user) {
        users.put(user.getUserId(), user);
    }

    public static User findUserById(String userId) {
        return users.get(userId);
    }

    public static Collection<User> findAll() {
        return users.values();
    }

    public static void add(User user) throws SQLException {
        Connection connection = DataBaseUtils.connectDB();

        String sql = "INSERT INTO user (userId, password, name, email) VALUES (?, ?, ?, ?)";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, user.getUserId());
        preparedStatement.setString(2, user.getPassword());
        preparedStatement.setString(3, user.getName());
        preparedStatement.setString(4, user.getEmail());
        preparedStatement.executeUpdate();

        preparedStatement.close();
        connection.close();
    }
}
