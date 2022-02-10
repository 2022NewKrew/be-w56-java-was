package repository;

import db.DatabaseConnector;
import mapper.MemoRowMapper;
import mapper.RowMapper;
import model.Memo;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class MemoRepository implements RepositoryInterface<Memo> {
    private final DatabaseConnector databaseConnector;
    private final RowMapper<Memo> rowMapper;

    private MemoRepository() {
        databaseConnector = DatabaseConnector.getInstance();
        rowMapper = new MemoRowMapper();
    }

    private static class MemoRepositoryHelper {
        private static final MemoRepository INSTANCE = new MemoRepository();
    }

    public static MemoRepository getInstance() {
        return MemoRepositoryHelper.INSTANCE;
    }


    @Override
    public void join(Memo target) throws SQLException {
        String sql = "insert into memo values(?, ?, ?)";
        databaseConnector.update(sql, target.getWriter(), target.getContent(), target.getDate());
    }

    @Override
    public void update(Memo target) throws SQLException {

    }

    @Override
    public Optional<Memo> findOne(String index) throws SQLException {
        return Optional.empty();
    }

    @Override
    public List<Memo> findAll() throws SQLException {
        String sql = "select * from memo order by date desc";
        return databaseConnector.queryAll(sql, rowMapper);
    }
}
