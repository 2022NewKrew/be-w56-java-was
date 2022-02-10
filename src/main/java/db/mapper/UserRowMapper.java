package db.mapper;

import webserver.domain.entity.User;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserRowMapper implements RowMapper<User> {
    @Override
    public User mapRow(ResultSet rs, int rowNum) throws SQLException {
        String userId = rs.getString("userId");
        String password = rs.getString("password");

        String name = rs.getString("name");
        String email = rs.getString("email");

        return new User(userId, password, name, email);
    }
}
