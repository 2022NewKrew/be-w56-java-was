package app.db;

import domain.model.User;
import lib.was.db.JdbcTemplate;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import lib.util.PropertiesLoader;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class JdbcTemplateTest {

    private JdbcTemplate subject;

    @BeforeAll
    static void setUpAll() throws IOException {
        PropertiesLoader.load();
    }

    @BeforeEach
    void setUp() {
        subject = new JdbcTemplate();
    }

    @AfterEach
    void tearDown() {
        subject.update("DELETE FROM posts", Collections.emptyList());
        subject.update("DELETE FROM users", Collections.emptyList());
    }

    @Test
    void queryForObject() {
        subject.update(
                "INSERT INTO users (userId, password, name, email) VALUES (?, ?, ?, ?)",
                List.of("user_id", "password", "name", "email")
        );

        User user = subject.queryForObject(
                "SELECT * FROM users WHERE userId = ?",
                Collections.singletonList("user_id"),
                new UserRowMapper()
        );

        assertEquals("user_id", user.getUserId());
    }

    @Test
    void queryForStream() {
        subject.update(
                "INSERT INTO users (userId, password, name, email) VALUES (?, ?, ?, ?)",
                List.of("user_id1", "password", "name", "email")
        );
        subject.update(
                "INSERT INTO users (userId, password, name, email) VALUES (?, ?, ?, ?)",
                List.of("user_id2", "password", "name", "email")
        );
        subject.update(
                "INSERT INTO users (userId, password, name, email) VALUES (?, ?, ?, ?)",
                List.of("user_id3", "password", "name", "email")
        );

        Stream<User> users = subject.queryForStream(
                "SELECT * FROM users",
                Collections.emptyList(),
                new UserRowMapper()
        );

        assertEquals(
                List.of("user_id1", "user_id2", "user_id3"),
                users.map(User::getUserId).collect(Collectors.toList())
        );
    }
}
