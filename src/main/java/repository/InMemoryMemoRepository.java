package repository;

import config.JdbcConfig;
import model.Memo;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class InMemoryMemoRepository {

    private static InMemoryMemoRepository instance;

    private final Connection connection;

    public static InMemoryMemoRepository getInstance() {
        if (instance == null) {
            instance = new InMemoryMemoRepository(JdbcConfig.getConnection());
            instance.createTable();
        }
        return instance;
    }

    private InMemoryMemoRepository(Connection connection) {
        this.connection = connection;
    }

    private void createTable() {
        String sql = "CREATE TABLE MEMO("
                + "id BIGINT PRIMARY KEY AUTO_INCREMENT,"
                + "created_at DATE NOT NULL,"
                + "writer VARCHAR(10) NOT NULL,"
                + "title VARCHAR(15) NOT NULL,"
                + "content VARCHAR(255) NOT NULL"
                + ");";

        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addMemo(Memo memo) {
        String sql = "INSERT INTO MEMO (created_at, writer, title, content)"
                + "VALUES ("
                + "'" + memo.getCreatedAt() + "',"
                + "'" + memo.getWriter() + "',"
                + "'" + memo.getTitle() + "',"
                + "'" + memo.getContent() + "'"
                + ");";

        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

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

    private Memo resultMappingMemo(ResultSet rs) throws SQLException {
        if (!rs.next()) {
            return null;
        }
        return new Memo(
                rs.getDate("created_at").toLocalDate(),
                rs.getString("writer"),
                rs.getString("title"),
                rs.getString("content"));
    }

    private List<Memo> resultMappingMemos(ResultSet rs) throws SQLException {
        List<Memo> memos = new ArrayList<>();
        Memo tmpMemo;
        while ((tmpMemo = resultMappingMemo(rs)) != null) {
            memos.add(tmpMemo);
        }
        return memos;
    }


}
