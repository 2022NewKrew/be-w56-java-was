package domain.db;

import domain.model.Memo;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

public class MemoRepository {

    private static Connection conn;

    static {
        try {
            conn = DriverManager.getConnection("jdbc:h2:mem:testdb2");
            Statement stmt = conn.createStatement();
            stmt.execute("create table if not exists memo(id BIGINT PRIMARY KEY AUTO_INCREMENT, author varchar(20), content varchar(255), createdAt TIMESTAMP default current_timestamp)");
            stmt.execute("insert into memo (author, content) values('bdf', '안녕')");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static List<Memo> findAll() {
        try (Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery("select * from memo");
            List<Memo> memos = new ArrayList<>();

            while (rs.next()) {
                Memo memo = new Memo();
                memo.setId(rs.getLong("id"));
                memo.setContent(rs.getString("content"));
                memo.setAuthor(rs.getString("author"));
                memo.setCreatedAt(rs.getTimestamp("createdAt").toLocalDateTime());
                memos.add(memo);
            }
            return memos;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void create(Memo memo) {
        try (Statement stmt = conn.createStatement()) {
            String sql = "insert into memo (author, content, createdAt) values(?,?,?)";
            PreparedStatement psmt = conn.prepareStatement(sql);

            psmt.setString(1, memo.getAuthor());
            psmt.setString(2, memo.getContent());
            GregorianCalendar gregorianCalendar = new GregorianCalendar();
            psmt.setTimestamp(3, new Timestamp(gregorianCalendar.getTimeInMillis()));
            psmt.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
