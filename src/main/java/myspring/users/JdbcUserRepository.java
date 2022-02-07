package myspring.users;

import webserver.annotations.Primary;
import webserver.annotations.Repository;
import webserver.configures.RowMapper;
import webserver.jdbc.JdbcTemplate;

import java.util.List;
import java.util.Optional;

import static java.util.Optional.ofNullable;

@Primary
@Repository
public class JdbcUserRepository implements UserRepository {

    private final JdbcTemplate jdbcTemplate;

    public JdbcUserRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void save(User user) {
        jdbcTemplate.update(
                "INSERT INTO users(id,password,name,email) VALUES (?,?,?,?)",
                user.getId(),
                user.getPassword(),
                user.getName(),
                user.getEmail()
        );
    }

    @Override
    public void update(User user) {
        jdbcTemplate.update(
                "UPDATE users SET password=?,name=?,email=? WHERE id=?",
                user.getPassword(),
                user.getName(),
                user.getEmail(),
                user.getId()
        );
    }

    @Override
    public Optional<User> validate(String id, String password) {
        List<User> results = jdbcTemplate.query(
                "SELECT * FROM users WHERE id=? AND password=?",
                mapper,
                id,
                password
        );
        return ofNullable(results.isEmpty() ? null : results.get(0));
    }

    @Override
    public Optional<User> findBySeq(long seq) {
        List<User> results = jdbcTemplate.query(
                "SELECT * FROM users WHERE seq=?",
                mapper,
                seq
        );
        return ofNullable(results.isEmpty() ? null : results.get(0));
    }

    @Override
    public Optional<User> findById(String id) {
        List<User> results = jdbcTemplate.query(
                "SELECT * FROM users WHERE id=?",
                mapper,
                id
        );
        return ofNullable(results.isEmpty() ? null : results.get(0));
    }

    @Override
    public Optional<User> findByEmail(String email) {
        System.out.println("TODO");
        return Optional.empty();
    }

    @Override
    public List<User> findAll() {
        return jdbcTemplate.query(
                "SELECT * FROM users ORDER BY seq DESC",
                mapper
        );
    }

    static RowMapper<User> mapper = (rs, rowNum) ->
            UserFactory.createUser(
                    rs.getLong("seq"),
                    rs.getString("id"),
                    rs.getString("password"),
                    rs.getString("name"),
                    rs.getString("email")
            );

}
