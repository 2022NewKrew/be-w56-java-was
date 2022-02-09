package app.db;

import domain.model.User;
import domain.repository.UserRepository;
import lib.was.db.JdbcTemplate;
import lib.was.di.Bean;
import lib.was.di.Inject;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Bean
public class JdbcUserRepository implements UserRepository {

    private final JdbcTemplate template;
    private final UserRowMapper mapper;

    @Inject
    public JdbcUserRepository(JdbcTemplate template, UserRowMapper mapper) {
        this.template = template;
        this.mapper = mapper;
    }

    public void addUser(User user) {
        String sql = "INSERT INTO users (userId, password, name, email) VALUES (?, ?, ?, ?)";
        List<Object> params = List.of(user.getUserId(), user.getPassword(), user.getName(), user.getEmail());
        template.update(sql, params);
    }

    public Optional<User> findUserById(String userId) {
        String sql = "SELECT * FROM users WHERE userId = ?";
        List<Object> params = Collections.singletonList(userId);
        try {
            return Optional.of(template.queryForObject(sql, params, mapper));
        } catch (RuntimeException e) {
            return Optional.empty();
        }
    }

    public List<User> findAllUsers() {
        String sql = "SELECT * FROM users";
        List<Object> params = Collections.emptyList();
        return template.queryForStream(sql, params, mapper).collect(Collectors.toList());
    }
}
