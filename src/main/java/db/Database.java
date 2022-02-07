package db;

import annotation.Bean;
import annotation.Inject;
import model.User;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Bean
public class Database {

    private final JdbcTemplate template;
    private final UserRowMapper mapper;

    @Inject
    public Database(JdbcTemplate template, UserRowMapper mapper) {
        this.template = template;
        this.mapper = mapper;
    }

    public void addUser(User user) {
        String sql = "INSERT INTO users (userId, password, name, email) VALUES (?, ?, ?, ?)";
        List<Object> params = List.of(user.getUserId(), user.getPassword(), user.getName(), user.getEmail());
        template.update(sql, params);
    }

    public User findUserById(String userId) {
        String sql = "SELECT * FROM users WHERE userId = ?";
        List<Object> params = Collections.singletonList(userId);
        return template.queryForObject(sql, params, mapper);
    }

    public Collection<User> findAll() {
        String sql = "SELECT * FROM users";
        List<Object> params = Collections.emptyList();
        return template.queryForStream(sql, params, mapper).collect(Collectors.toList());
    }
}
