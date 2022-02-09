package domain.memo.repository;

import domain.memo.model.Memo;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class H2MemoRepository implements MemoRepository {

    private static H2MemoRepository instance;

    private final Connection connection;

    public static H2MemoRepository get() {
        if (instance == null) {
            instance = new H2MemoRepository(getConnection());
            instance.createTable();
        }
        return instance;
    }

    private H2MemoRepository(Connection connection) {
        this.connection = connection;
    }

    private static Connection getConnection() {
        try {
            return DriverManager.getConnection("jdbc:h2:~/mem", "sa", "sa");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    private void createTable() {
        String dropSql = "DROP TABLE IF EXISTS MEMO";
        String sql = "CREATE TABLE MEMO("
            + "id BIGINT PRIMARY KEY AUTO_INCREMENT,"
            + "created_at DATE NOT NULL,"
            + "author VARCHAR(10) NOT NULL,"
            + "content VARCHAR(255) NOT NULL"
            + ");";

        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(dropSql);
            statement.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void save(Memo memo) {
        String sql = "INSERT INTO MEMO (created_at, author, content)"
            + "VALUES ("
            + "'" + Date.valueOf(memo.getCreatedAt()) + "',"
            + "'" + memo.getAuthor() + "',"
            + "'" + memo.getContent() + "'"
            + ");";

        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Collection<Memo> findAll() {
        String sql = "SELECT * FROM MEMO ORDER BY created_at DESC";

        try (Statement statement = connection.createStatement()) {
            ResultSet rs = statement.executeQuery(sql);
            return resultMappingMemos(rs);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return List.of();
    }

    private List<Memo> resultMappingMemos(ResultSet rs) throws SQLException {
        List<Memo> memos = new ArrayList<>();
        while (rs.next()) {
            memos.add(Memo.builder()
                .createdAt(rs.getDate("created_at").toLocalDate())
                .author(rs.getString("author"))
                .content(rs.getString("content"))
                .build());
        }
        return memos;
    }
}
