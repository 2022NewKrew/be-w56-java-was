package adaptor.out.persistence.mysql.memo;

import adaptor.out.persistence.mysql.QueryBuilder;
import application.out.memo.MemoDao;
import domain.memo.Memo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static infrastructure.config.DatabaseConfig.*;

public class MemoMysqlDao implements MemoDao {

    private static final Logger log = LoggerFactory.getLogger(MemoMysqlDao.class);
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_WRITER = "writer";
    private static final String COLUMN_CONTENT = "content";
    private static final String COLUMN_CREATED_AT = "created_at";
    private final QueryBuilder queryBuilder;

    public MemoMysqlDao(QueryBuilder queryBuilder) {
        this.queryBuilder = queryBuilder;
    }

    @Override
    public void save(Memo memo) {
        try (Connection conn = DriverManager.getConnection(DB_URL, USER_NAME, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(queryBuilder.insertOne(COLUMN_ID, COLUMN_WRITER, COLUMN_CONTENT, COLUMN_CREATED_AT))
        ) {
            pstmt.setInt(1, memo.getId());
            pstmt.setString(2, memo.getWriter());
            pstmt.setString(3, memo.getContent());
            pstmt.setTimestamp(4, Timestamp.valueOf(memo.getCreatedAt()));

            pstmt.executeUpdate();
        } catch (SQLException e) {
            log.debug(e.getMessage());
        }
    }

    @Override
    public Memo findOneById(int id) {
        try (Connection conn = DriverManager.getConnection(DB_URL, USER_NAME, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(queryBuilder.selectOne(COLUMN_ID))
        ) {
            pstmt.setInt(1, id);
            ResultSet resultSet = pstmt.executeQuery();

            while (resultSet.next()) {
                return Memo.builder()
                        .id(resultSet.getInt(COLUMN_ID))
                        .writer(resultSet.getString(COLUMN_WRITER))
                        .content(resultSet.getString(COLUMN_CONTENT))
                        .createdAt(resultSet.getTimestamp(COLUMN_CREATED_AT).toLocalDateTime())
                        .build();
            }
        } catch (SQLException e) {
            log.debug(e.getMessage());
        }
        return null;
    }

    @Override
    public List<Memo> findAll() {
        List<Memo> memos = new ArrayList<>();

        try (Connection conn = DriverManager.getConnection(DB_URL, USER_NAME, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(queryBuilder.selectAll())
        ) {
            ResultSet resultSet = pstmt.executeQuery();

            while (resultSet.next()) {
                memos.add(Memo.builder()
                        .id(resultSet.getInt(COLUMN_ID))
                        .content(resultSet.getString(COLUMN_CONTENT))
                        .createdAt(resultSet.getTimestamp(COLUMN_CREATED_AT).toLocalDateTime())
                        .build());
            }
        } catch (SQLException e) {
            log.debug(e.getMessage());
        }
        return memos;
    }
}
