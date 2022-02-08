package db;

import annotation.Bean;
import model.User;

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
