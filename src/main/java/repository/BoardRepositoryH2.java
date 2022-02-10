package repository;

import db.MyJdbcTemplate;
import db.MyKeyHolder;
import model.Board;
import repository.mapper.BoardRowMapper;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Objects;

public class BoardRepositoryH2 implements BoardRepository {

    private static final int FAIL = 0;
    private final MyJdbcTemplate jdbcTemplate = new MyJdbcTemplate();

    @Override
    public Long save(Board board) throws SQLException {
        String sql = "INSERT INTO board(title, writer, contents) values (?,?,?)";
        MyKeyHolder keyHolder = new MyKeyHolder();

        int rs = jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, board.getTitle());
            ps.setString(2, board.getWriter());
            ps.setString(3, board.getContents());

            return ps;
        }, keyHolder);

        if (rs == FAIL) {
            throw new SQLException("BOARD TABLE SAVE FAIL");
        }

        return Objects.requireNonNull(keyHolder.getKey()).longValue();
    }

    @Override
    public List<Board> findAll() throws SQLException {
        String sql = "SELECT writer, title, contents, create_time FROM board";
        return jdbcTemplate.query(sql, new BoardRowMapper());
    }
}
