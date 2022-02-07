package webserver.repository;

import db.ConnectionManager;
import model.Memo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class MemoRepository implements CrudRepository<Memo, Long> {

    private static final Logger log = LoggerFactory.getLogger(MemoRepository.class);

    private final ConnectionManager connectionManager = ConnectionManager.getInstance();

    @Override
    public void delete(Memo entity) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Iterable<Memo> findAll() {
        String sql = "select m.id, u.name as author, m.author_id, m.content, m.created_at " +
                "from memo m " +
                "left join users u " +
                "on m.author_id = u.id " +
                "order by m.id desc";
        Connection conn = null;
        try {
            conn = connectionManager.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();

            List<Memo> result = new ArrayList<>();
            while (rs.next()) {
                long id = rs.getLong("id");
                String author = rs.getString("author");
                Long authorId = rs.getLong("author_id");
                String content = rs.getString("content");
                LocalDate createdAt = rs.getDate("created_at").toLocalDate();
                Memo memo = new Memo(id, author, authorId, content, createdAt);
                result.add(memo);
            }
            return result;

        } catch (SQLException e) {
            log.error("could not find entity : {}", e.getMessage());
            e.printStackTrace();
        } finally {
            connectionManager.retrieveConnection(conn);
        }
        return Collections.emptyList();
    }

    @Override
    public Optional<Memo> findById(Long id) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Memo save(Memo entity) {
        String sql = "insert into memo (author_id, content, created_at) values ( ?, ?, ? )";
        Connection conn = null;
        try {
            conn = connectionManager.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            pstmt.setLong(1, entity.getAuthorId());
            pstmt.setString(2, entity.getContent());
            pstmt.setDate(3, Date.valueOf(entity.getCreatedAt()));
            pstmt.execute();
            ResultSet rs = pstmt.getGeneratedKeys();

            if (rs.next()) {
                long id = rs.getLong(1);
                entity.setId(id);
            }

        } catch (SQLException e) {
            log.error("could not save entity : {}", e.getMessage());
            e.printStackTrace();
        } finally {
            connectionManager.retrieveConnection(conn);
        }
        return entity;
    }

    @Override
    public Memo update(Memo entity) {
        throw new UnsupportedOperationException();
    }
}
