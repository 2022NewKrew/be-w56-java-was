package db;

import config.DataSourceConfig;
import java.util.List;
import java.util.Optional;
import jdbc.JdbcTemplate;
import jdbc.RowMapper;
import model.User;

public class UserRepository {

    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<User> userRowMapper = getUserRowMapper();

    public UserRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void addUser(User user) {
        String sql = "insert into users (id, password, name, email) values (?, ?, ?, ?)";
        jdbcTemplate.update(
            sql,
            user.getUserId(),
            user.getPassword(),
            user.getName(),
            user.getEmail()
        );
    }

    public Optional<User> findById(String userId) {
        String sql = "select * from users where id = ?";
        return jdbcTemplate.queryForObject(sql, userRowMapper, userId);
    }

    public List<User> findAll() {
        String sql = "select * from users";
        return jdbcTemplate.query(sql, userRowMapper);
    }

    private RowMapper<User> getUserRowMapper() {
        return resultSet -> new User(
            resultSet.getString(1),
            resultSet.getString(2),
            resultSet.getString(3),
            resultSet.getString(4));
    }
}
