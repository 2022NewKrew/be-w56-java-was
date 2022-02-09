package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Map;
import java.util.Properties;

import com.google.common.collect.Maps;

import model.User;
import util.PropertiesUtils;

public class DataBase {
    private static Connection connection;
    private static final Map<String, User> users = Maps.newHashMap();

    public static void connect(){
        Properties prop = PropertiesUtils.readDBProperties();
        try{
            assert prop != null;
            String url = prop.getProperty("url");
            String userName = prop.getProperty("userName");
            String password = prop.getProperty("password");
            connection = DriverManager.getConnection(url, userName, password);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void close() throws SQLException {
        connection.close();
    }

    public static void addUser(User user) {
        users.put(user.getUserId(), user);
    }

    public static User findUserById(String userId) {
        return users.get(userId);
    }

    public static Collection<User> findAll() {
        return users.values();
    }
}
