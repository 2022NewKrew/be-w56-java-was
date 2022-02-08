package repository;

import db.JdbcTemplate;
import db.RowMapper;
import model.Memo;
import model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;

public class MemoRepository {
    private static final MemoRepository INSTANCE = new MemoRepository();

    public static MemoRepository getInstance() {
        return INSTANCE;
    }

    private MemoRepository() {
        jdbcTemplate = JdbcTemplate.getInstance();
        mapper = new MemoMapper();
    }

    private final JdbcTemplate jdbcTemplate;
    private final MemoMapper mapper;

    public void save(Memo memo) {
        String sql = "insert into memo (writer_id, content) values (?, ?)";
        jdbcTemplate.update(sql, memo.getWriter().getUserId(), memo.getContent());
    }

    public Collection<Memo> findAll() {
        String sql = "select * from memo join user on memo.writer_id=user.user_id order by memo_id desc";
        return jdbcTemplate.query(sql, mapper);
    }

    private static class MemoMapper implements RowMapper<Memo> {
        @Override
        public Memo mapRow(ResultSet rs, int rowNum) throws SQLException {
            return Memo.builder()
                    .memoId(rs.getLong("memo_id"))
                    .writer(User.builder()
                            .userId(rs.getString("user_id"))
                            .password(rs.getString("password"))
                            .name(rs.getString("name"))
                            .email(rs.getString("email"))
                            .build())
                    .content(rs.getString("content"))
                    .regDate(rs.getTimestamp("reg_date").toLocalDateTime())
                    .build();
        }
    }
}
