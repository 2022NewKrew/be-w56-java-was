package db;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import model.Memo;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DataBase {
//    private static Map<String, User> users = Maps.newHashMap();
    private static Logger log = LoggerFactory.getLogger(DataBase.class);
    private static Connection conn = null;

    private static String DRIVER = "org.h2.Driver";
    private static String DB_URL = "jdbc:h2:~/test";
    private static String DB_ID = "sa";
    private static String DB_PW = "";

    static {
        try {
            Class.forName(DRIVER);
            conn = DriverManager.getConnection(DB_URL, DB_ID, DB_PW);

            String memberSql = "CREATE TABLE IF NOT EXISTS member (\n"
                    + "userId varchar(30) PRIMARY KEY,\n"
                    + "password varchar(30) NOT NULL,\n"
                    + "name varchar(30) NOT NULL,\n"
                    + "email varchar(50) NOT NULL\n"
                    + ")\n";
            String memoSql = "CREATE TABLE IF NOT EXISTS memo (\n"
                    + "memoId INT AUTO_INCREMENT PRIMARY KEY,\n"
                    + "date TIMESTAMP DEFAULT NOW(),\n"
                    + "writer varchar(30) NOT NULL,\n"
                    + "memo varchar(100) NOT NULL,\n"
                    + "FOREIGN KEY(writer) REFERENCES member(userId)\n"
                    + ")\n";
            conn.prepareStatement(memberSql).execute();
            conn.prepareStatement(memoSql).execute();

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    public static void addUser(User user) {
        String sql = "INSERT INTO member (userId, password, name, email)\n"
                + "VALUES (?,?,?,?)";
        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1,user.getUserId());
            pstmt.setString(2,user.getPassword());
            pstmt.setString(3,user.getName());
            pstmt.setString(4,user.getEmail());

            boolean addResult = pstmt.execute();
            log.info(">>>> Database addUser = " + addResult);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static User findUserById(String userId) {
        User resultUser = null;
        String sql = "SELECT password, name, email FROM member\n"
                + "WHERE userId=?";
        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1,userId);

            ResultSet rs = pstmt.executeQuery();
            if(rs.next()) {
                String password = rs.getString("password");
                String name = rs.getString("name");
                String email = rs.getString("email");
                resultUser = new User(userId, password, name, email);
            }
            log.info(">>>> Database findUserById");

        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultUser;
    }

    public static List<User> findUserAll() {
        List<User> resultUser = new ArrayList<>();
        String sql = "SELECT userId, password, name, email FROM member\n";

        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();

            while(rs.next()) {
                String userId = rs.getString("userId");
                String name = rs.getString("name");
                String email = rs.getString("email");
                resultUser.add(new User(userId, name, email));
            }
            log.info(">>>> Database findUserAll");

        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultUser;
    }

    public static void addMemo (Memo memo) {
        String sql = "INSERT INTO memo(writer, memo)\n" +
                "VALUES (?,?)\n";

        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1,memo.getWriter());
            pstmt.setString(2,memo.getMemo());

            pstmt.execute();
            log.info(">>>> Database addMemo");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static List<Memo> findMemoAll () {
        List<Memo> resultMemo = new ArrayList<>();
        String sql = "SELECT memoId, SUBSTR(date, 1, 10) as date, writer, memo FROM memo\n";

        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();

            while(rs.next()) {
                int memoId = rs.getInt("memoId");
                String date = rs.getString("date");
                String writer = rs.getString("writer");
                String memoContent = rs.getString("memo");
                resultMemo.add(new Memo(memoId,date,writer,memoContent));
            }
            log.info(">>>> Database findMemoAll");

        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultMemo;
    }
}
