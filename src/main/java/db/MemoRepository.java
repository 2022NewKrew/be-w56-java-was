package db;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import jdbc.JdbcTemplate;
import jdbc.RowMapper;
import model.Memo;

public class MemoRepository {

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private final RowMapper<Memo> memoRowMapper = getMemoRowMapper();
    private final JdbcTemplate jdbcTemplate;

    public MemoRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void save(Memo memo) {
        String sql = "insert into memos (user_id, memo, created_at) values(?, ?, ?)";
        jdbcTemplate.update(sql,
            memo.getUserId(),
            memo.getMemo(),
            memo.getCreatedAt());
    }

    public List<Memo> findAll() {
        String sql = "select * from memos order by id desc ";
        return jdbcTemplate.query(sql, memoRowMapper);
    }

    public RowMapper<Memo> getMemoRowMapper() {
        return resultSet -> new Memo(
            resultSet.getLong(1),
            resultSet.getString(2),
            resultSet.getString(3),
            LocalDate.parse(resultSet.getString(4), formatter)
        );
    }
}
