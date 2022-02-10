package springmvc.db;

import model.Memo;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class MemoDataBase {

    private static Connection con;
    private static PreparedStatement pstmt;

    public static void save(Memo memo) throws SQLException {
        con = DriverManager.getConnection(JdbcConstant.DB_URL, JdbcConstant.USER, JdbcConstant.PASS);
        pstmt = con.prepareStatement("insert into memo(name, content, date) values (?, ?, ?)");
        pstmt.setString(1, memo.getName());
        pstmt.setString(2, memo.getContent());
        pstmt.setObject(3, memo.getDate());
        pstmt.executeUpdate();
        pstmt.close();
        con.close();
    }

    public static List<Memo> findAll() throws SQLException {
        List<Memo> list = new ArrayList<>();
        con = DriverManager.getConnection(JdbcConstant.DB_URL, JdbcConstant.USER, JdbcConstant.PASS);
        pstmt = con.prepareStatement("select * from memo order by date desc");

        ResultSet rs = pstmt.executeQuery();
        while (rs.next()) {
            String name = rs.getString("name");
            String content = rs.getString("content");
            LocalDateTime date = rs.getObject("date", LocalDateTime.class);
            list.add(new Memo(name, content, date));
        }
        rs.close();
        pstmt.close();
        con.close();
        return list;
    }
}
