package app.db;

import domain.model.User;
import lib.util.PropertiesLoader;
import lib.was.db.JdbcTemplate;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class JdbcUserRepositoryTest {

    private JdbcTemplate template;
    private JdbcUserRepository subject;

    @BeforeAll
    static void setUpAll() throws IOException {
        PropertiesLoader.load();
    }

    @BeforeEach
    void setUp() {
        template = new JdbcTemplate();
        subject = new JdbcUserRepository(template, new UserRowMapper());
    }

    @AfterEach
    void tearDown() {
        template.update("DELETE FROM posts", Collections.emptyList());
        template.update("DELETE FROM users", Collections.emptyList());
    }

    @Test
    void addUser() {
        User user = new User(0, "userId", "password", "name", "email");

        subject.addUser(user);

        assertEquals(
                Stream.of(user)
                        .map(User::getUserId)
                        .collect(Collectors.toList()),
                template.queryForStream("SELECT * FROM users", Collections.emptyList(), new UserRowMapper())
                        .map(User::getUserId)
                        .collect(Collectors.toList())
        );
    }

    @Test
    void findUserById() {
        template.update(
                "INSERT INTO users (userId, password, name, email) VALUES (?, ?, ?, ?)",
                List.of("user_id", "password", "name", "email")
        );

        Optional<User> user = subject.findUserById("user_id");

        assertEquals("name", user.map(User::getName).orElse(""));
    }

    @Test
    void findAll() {
        template.update(
                "INSERT INTO users (userId, password, name, email) VALUES (?, ?, ?, ?)",
                List.of("user_id1", "password", "name", "email")
        );
        template.update(
                "INSERT INTO users (userId, password, name, email) VALUES (?, ?, ?, ?)",
                List.of("user_id2", "password", "name", "email")
        );
        template.update(
                "INSERT INTO users (userId, password, name, email) VALUES (?, ?, ?, ?)",
                List.of("user_id3", "password", "name", "email")
        );

        Collection<User> users = subject.findAllUsers();

        assertEquals(
                List.of("user_id1", "user_id2", "user_id3"),
                users.stream().map(User::getUserId).collect(Collectors.toList())
        );
    }
}
