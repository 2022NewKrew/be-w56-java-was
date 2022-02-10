package db;

import java.sql.*;
import java.util.*;

import util.PropertiesUtils;

public class DataBase {
    private static Connection connection;

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

    public static Connection getConnection() {
        return connection;
    }
}
