package repository;

import db.MyJdbcTemplate;
import db.MyKeyHolder;
import model.Member;
import repository.mapper.MemberRowMapper;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Objects;

public class MemberRepositoryH2 implements MemberRepository {

    private static final int FAIL = 0;
    private final MyJdbcTemplate jdbcTemplate = new MyJdbcTemplate();

    @Override
    public Long save(Member member) throws SQLException {

        String sql = "INSERT INTO member(user_id, password, name, email) values (?,?,?,?)";
        MyKeyHolder keyHolder = new MyKeyHolder();

        int rs = jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, member.getUserId());
            ps.setString(2, member.getPassword());
            ps.setString(3, member.getName());
            ps.setString(4, member.getEmail());

            return ps;
        }, keyHolder);

        if(rs == FAIL) {
            throw new SQLException("USER TABLE SAVE FAIL");
        }

        return Objects.requireNonNull(keyHolder.getKey()).longValue();
    }

    @Override
    public List<Member> findAll() throws SQLException {

        String sql = "SELECT user_id, password, name, email FROM member";

        return jdbcTemplate.query(sql, new MemberRowMapper());
    }
}
