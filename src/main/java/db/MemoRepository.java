package db;

import model.Memo;
import model.User;
import util.DBUtils;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class MemoRepository {

    private final Connection connection;

    private MemoRepository(Connection connection) {
        this.connection = connection;
        createTable();
    }

    private static class LazyHolder {
        private static final MemoRepository userRepository = new MemoRepository(DBUtils.getConnection());
    }

    public static MemoRepository getInstance() {
        return LazyHolder.userRepository;
    }

    private void createTable() {
        String sql = "CREATE TABLE IF NOT EXISTS MEMO (" +
                "ID BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY," +
                "AUTHOR VARCHAR(20) NOT NULL," +
                "CONTENT VARCHAR(50) NOT NULL," +
                "CREATE_AT DATE NOT NULL)";

        try (Statement stmt = connection.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addMemo(Memo memo) {
        String sql = "INSERT INTO MEMO(AUTHOR, CONTENT, CREATE_AT) VALUES (?, ?, ?)";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, memo.getAuthor());
            pstmt.setString(2, memo.getContent());
            pstmt.setObject(3, memo.getCreateAt());
            pstmt.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Memo> findAll() {
        String sql = "SELECT ID, AUTHOR, CONTENT, CREATE_AT FROM MEMO ORDER BY ID DESC";
        List<Memo> memos = new ArrayList<>();

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                memos.add(new Memo(
                        rs.getInt(1),
                        rs.getString(2),
                        rs.getString(3),
                        rs.getObject(4, LocalDate.class)));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return memos;
    }
}
