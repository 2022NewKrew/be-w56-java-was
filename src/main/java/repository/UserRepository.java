package repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Optional;

import db.JdbcTemplate;
import db.RowMapper;
import lombok.extern.slf4j.Slf4j;
import model.User;

@Slf4j
public class UserRepository {
    private static final UserRepository INSTANCE = new UserRepository();

    public static UserRepository getInstance() {
        return INSTANCE;
    }

    private UserRepository() {
        jdbcTemplate = JdbcTemplate.getInstance();
        mapper = new UserMapper();
    }

    private final JdbcTemplate jdbcTemplate;
    private final UserMapper mapper;

    public void save(User user) {
        String sql = "insert into user (user_id, password, name, email) values (?, ?, ?, ?)";
        jdbcTemplate.update(sql, user.getUserId(), user.getPassword(), user.getName(), user.getEmail());
    }

    public void update(User user) {
        String sql = "update user set password=?, name=?, email=? where user_id=?";
        jdbcTemplate.update(sql, user.getPassword(), user.getName(), user.getEmail(), user.getUserId());
    }

    public Optional<User> findById(String userId) {
        String sql = "select * from user where user_id = ?";
        User user = jdbcTemplate.queryForObject(sql, mapper, userId);
        return Optional.ofNullable(user);
    }

    public Collection<User> findAll() {
        String sql = "select * from user";
        return jdbcTemplate.query(sql, mapper);
    }

    private static class UserMapper implements RowMapper<User> {
        @Override
        public User mapRow(ResultSet rs, int rowNum) throws SQLException {
            return User.builder()
                    .userId(rs.getString("user_id"))
                    .password(rs.getString("password"))
                    .name(rs.getString("name"))
                    .email(rs.getString("email"))
                    .build();
        }
    }
}
