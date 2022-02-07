package db;

import model.User;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import webserver.PropertiesLoader;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

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
    }
}
