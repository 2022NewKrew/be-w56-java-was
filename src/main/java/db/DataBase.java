package db;

import java.sql.*;
import java.util.Collection;
import java.util.Map;

import com.google.common.collect.Maps;

import com.mysql.cj.protocol.Resultset;
import model.User;

public class DataBase {
    private static Map<String, User> users = Maps.newHashMap();
    private Connection conn;

    public DataBase(){
        init();
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


    public void save(User user) {
        System.out.println(user);
        try {
            String sql = "insert into users(userId, password, userName, email) values (?, ?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, user.getUserId());
            ps.setString(2, user.getPassword());
            ps.setString(3, user.getName());
            ps.setString(4, user.getEmail());
            ps.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void init(){
        try {

            String url = "jdbc:mysql://localhost:3306/mydb?useSSL=false&allowPublicKeyRetrieval=true";
            String id = "root";
            String pw = "root";

            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(url, id, pw);
            Statement statement = conn.createStatement();
            statement.execute("drop table if exists users;");
            statement.execute(
                    "create table if not exists users(" +
                            "id int auto_increment," +
                            " userId varchar(32)," +
                            " password varchar(32)," +
                            " userName varchar(32)," +
                            " email varchar(64)," +
                            " primary key(id)," +
                            " unique key(userId));");
        }
        catch (Exception e){
            e.printStackTrace();
        }

    }
}