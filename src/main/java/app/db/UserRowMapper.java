package app.db;

import lib.was.db.RowMapper;
import lib.was.di.Bean;
import domain.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;

@Bean
public class UserRowMapper implements RowMapper<User> {

    @Override
    public User map(ResultSet rs) throws SQLException {
        return new User(
                rs.getInt("id"),
                rs.getString("userId"),
                rs.getString("password"),
                rs.getString("name"),
                rs.getString("email")
        );
    }
}
