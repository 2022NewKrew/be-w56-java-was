package db;

import annotation.Bean;
import annotation.Inject;
import model.User;

import java.util.Collection;

@Bean
public class Database {

    private final JdbcTemplate template;

    @Inject
    public Database(JdbcTemplate template) {
        this.template = template;
    }

    public void addUser(User user) {

    }

    public User findUserById(String userId) {
        return null;
    }

    public Collection<User> findAll() {
        return null;
    }
}
