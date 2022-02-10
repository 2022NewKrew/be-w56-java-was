package application.db;

import application.domain.Memo;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class MemoRepository {

    private final DataSource dataSource;

    public MemoRepository(DataSource dataSource) {
        this.dataSource = dataSource;

        createMemoTable();
        Memo memo = new Memo("testID", "안녕하세요~!~!~", LocalDate.now());
        addMemo(memo);
    }

    public void addMemo(Memo memo) {
        final String sql = "INSERT INTO memos(`userId`, `content`, `date`) VALUES(?,?,?);";

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = getConnection();
            pstmt = conn.prepareStatement(sql);

            pstmt.setString(1, memo.getUserId());
            pstmt.setString(2, memo.getContent());
            pstmt.setDate(3, Date.valueOf(memo.getLocalDate()));

            pstmt.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(conn, pstmt, rs);
        }

    }

    public Memo findMemoById(String userId) {
        final String sql = "SELECT * FROM memos WHERE userId = ?";

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = getConnection();
            pstmt = conn.prepareStatement(sql);

            pstmt.setString(1, userId);

            rs = pstmt.executeQuery();

            return memoMapper(rs);

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public List<Memo> findAll() {
        final String sql = "SELECT * FROM memos";

        Connection conn = null;
        ResultSet rs = null;

        List<Memo> memos = new ArrayList<>();

        try {
            conn = getConnection();
            Statement stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);

            while (rs.next()) {
                memos.add(memoMapper(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return memos;
    }

    private Memo memoMapper(ResultSet rs) throws SQLException {
        return new Memo(rs.getString("userId"), rs.getString("content"), rs.getDate("date").toLocalDate());
    }

    private void createMemoTable() {
        Connection conn = null;
        Statement statement = null;

        final String sql = "drop table if exists memos CASCADE;" +
                " CREATE TABLE memos (" +
                " id bigint PRIMARY KEY AUTO_INCREMENT," +
                " userId varchar(125)," +
                " content TEXT," +
                " date DATE NOT NULL," +
                " foreign key (userId) references users(userId) on delete CASCADE" +
                ");";

        try {
            conn = getConnection();
            statement = conn.createStatement();
            statement.execute(sql);
            statement.close();
            DataSourceUtils.releaseConnection(conn);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private Connection getConnection() throws SQLException {
        return DataSourceUtils.getConnection(dataSource);
    }

    private void close(Connection conn, PreparedStatement pstmt, ResultSet rs) {
        try {
            if (rs != null) {
                rs.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } try {
            if (pstmt != null) {
                pstmt.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (conn != null) {
            close(conn);
        }
    }

    private void close(Connection conn) {
        DataSourceUtils.releaseConnection(conn);
    }
}
