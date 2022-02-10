package db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.atomic.AtomicLong;
import model.Memo;

public class MemoStorage {

    private static final DBConnection dbConn = new DBConnection();
    private static final AtomicLong memoId = new AtomicLong();

    public static void addMemo(Memo memo) throws SQLException, ClassNotFoundException {
        memo.setId(memoId.getAndIncrement());

        dbConn.connect();
        String sql = "INSERT INTO memo VALUES(" +
                     "'" + memo.getId() + "'" + "," +
                     "'" + memo.getWriter() + "'" + "," +
                     "'" + memo.getContent() + "'" + "," +
                     "'" + Timestamp.valueOf(memo.getCreatedAt()) + "'" + ")";
        Statement statement = dbConn.getConnection().createStatement();
        statement.executeUpdate(sql, Statement.NO_GENERATED_KEYS);
        statement.close();
        dbConn.close();
    }

    public static Collection<Memo> findAll() throws SQLException, ClassNotFoundException {
        ArrayList<Memo> memoList = new ArrayList<>();
        dbConn.connect();
        String sql = "SELECT * FROM memo";
        Statement statement = dbConn.getConnection().createStatement();
        ResultSet rs = statement.executeQuery(sql);

        while (rs.next()) {
            long id = Long.parseLong(rs.getString("id"));
            String writer = rs.getString("writer");
            String content = rs.getString("content");
            LocalDateTime createdAt = rs.getTimestamp("createdAt").toLocalDateTime();
            Memo memo = new Memo(writer, content, createdAt);
            memo.setId(id);
            memoList.add(memo);
        }

        statement.close();
        dbConn.close();
        return memoList;
    }
}
