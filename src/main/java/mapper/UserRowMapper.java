package mapper;

import model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class UserRowMapper implements RowMapper<User> {
    @Override
    public Optional<User> mapRow(ResultSet resultSet) throws SQLException {
        return Optional.of(User.builder()
                .userId(resultSet.getString("userId"))
                .password(resultSet.getString("password"))
                .name(resultSet.getString("name"))
                .email(resultSet.getString("email"))
                .build());
    }
}
