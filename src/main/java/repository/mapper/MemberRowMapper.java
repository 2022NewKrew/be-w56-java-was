package repository.mapper;

import model.Member;

import java.sql.ResultSet;
import java.sql.SQLException;

public class MemberRowMapper implements MyRowMapper<Member> {

    @Override
    public Member mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new Member.Builder()
                .userId(rs.getString("user_id"))
                .name(rs.getString("name"))
                .password(rs.getString("password"))
                .email(rs.getString("email"))
                .build();
    }
}
