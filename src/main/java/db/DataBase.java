package db;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.google.common.collect.Maps;

import model.Post;
import model.User;

public class DataBase {
    private static String DRIVER = "org.h2.Driver";
    private static String DB_URL = "jdbc:h2:mem:default";
    private static String DB_ID = "sa";
    private static String DB_PW = "";

    private static Connection conn = null;

    static {
        try {
            Class.forName(DRIVER);
            conn = DriverManager.getConnection(DB_URL, DB_ID, DB_PW);

            String userSql = "CREATE TABLE IF NOT EXISTS users (\n"
                    + "userId varchar(30) PRIMARY KEY,\n"
                    + "password varchar(30) NOT NULL,\n"
                    + "name varchar(30) NOT NULL,\n"
                    + "email varchar(50) NOT NULL\n"
                    + ")\n";
            String postSql = "CREATE TABLE IF NOT EXISTS post (\n"
                    + "postId INT AUTO_INCREMENT PRIMARY KEY,\n"
                    + "createdAt DATE DEFAULT NOW(),\n"
                    + "writer varchar(30) NOT NULL,\n"
                    + "content varchar(100) NOT NULL,\n"
                    + "FOREIGN KEY(writer) REFERENCES users(userId)\n"
                    + ")\n";
            String defaultUser1 = "INSERT INTO users (userId, password, name, email)\n"
                    + "VALUES ('a','a','a','a')";
            String defaultUser2 = "INSERT INTO users (userId, password, name, email)\n"
                    + "VALUES ('b','b','b','b')";
            String defaultPost1 = "INSERT INTO post(writer, content)\n" +
                    "VALUES ('a','aaaaaaaaaaaa')\n";
            String defaultPost2 = "INSERT INTO post(writer, content)\n" +
                    "VALUES ('b','bbbbbbbbbbbb')\n";

            conn.prepareStatement(userSql).execute();
            conn.prepareStatement(postSql).execute();
            conn.prepareStatement(defaultUser1).execute();
            conn.prepareStatement(defaultUser2).execute();
            conn.prepareStatement(defaultPost1).execute();
            conn.prepareStatement(defaultPost2).execute();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void createUser(User user){
        String sql = "INSERT INTO users (userId, password, name, email)\n"
                + "VALUES (?,?,?,?)";
        try{
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, user.getUserId());
            ps.setString(2, user.getPassword());
            ps.setString(3, user.getName());
            ps.setString(4, user.getEmail());

            ps.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static User findUserById(String userId){
        User user = null;
        String sql = "SELECT password, name, email FROM users\n"
                + "WHERE userId=?";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1,userId);

            ResultSet rs = ps.executeQuery();
            if(rs.next()) {
                String password = rs.getString("password");
                String name = rs.getString("name");
                String email = rs.getString("email");
                user = new User(userId, password, name, email);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return user;
    }

    public static List<User> findUsers(){
        List<User> users = new ArrayList<>();
        String sql = "SELECT userId, password, name, email FROM users\n";

        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while(rs.next()) {
                String userId = rs.getString("userId");
                String password = rs.getString("password");
                String name = rs.getString("name");
                String email = rs.getString("email");
                users.add(new User(userId, password, name, email));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return users;
    }
    public static void createMemo (Post post) {
        String sql = "INSERT INTO post(writer, content)\n" +
                "VALUES (?,?)\n";

        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1,post.getWriter());
            ps.setString(2,post.getContent());
            ps.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static List<Post> findPosts () {
        List<Post> posts = new ArrayList<>();
        String sql = "SELECT postId, createdAt, writer, content FROM post\n";

        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while(rs.next()) {
                int postId = rs.getInt("postId");
                LocalDate date = rs.getDate("createdAt").toLocalDate();
                String writer = rs.getString("writer");
                String content = rs.getString("content");
                posts.add(new Post(postId,date,writer,content));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return posts;
    }
}
