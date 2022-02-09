package app.db;

import domain.model.Post;
import domain.model.User;
import lib.util.PropertiesLoader;
import lib.was.db.JdbcTemplate;
import lib.was.db.UpdateResult;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class JdbcPostRepositoryTest {

    private JdbcTemplate template;
    private JdbcPostRepository subject;

    @BeforeAll
    static void setUpAll() throws IOException {
        PropertiesLoader.load();
    }

    @BeforeEach
    void setUp() {
        template = new JdbcTemplate();
        subject = new JdbcPostRepository(template, new PostRowMapper());
    }

    @AfterEach
    void tearDown() {
        template.update("DELETE FROM posts", Collections.emptyList());
        template.update("DELETE FROM users", Collections.emptyList());
    }

    @Test
    void addPost() {
        UpdateResult result = template.update(
                "INSERT INTO users (userId, password, name, email) VALUES (?, ?, ?, ?)",
                List.of("user_id", "password", "name", "email")
        );
        template.update(
                "INSERT INTO posts (authorId, title, content) VALUES (?, ?, ?)",
                List.of(result.getKey(), "title", "content")
        );

        subject.addPost(
                new Post.Builder()
                        .author(new User(result.getKey(), "user_id", "password", "name", "email"))
                        .title("title")
                        .content("content")
                        .build()
        );

        assertEquals(
                "title",
                template.queryForObject(
                        "SELECT * FROM posts INNER JOIN users ON users.id = posts.authorId WHERE posts.authorId = ?",
                        List.of(result.getKey()),
                        new PostRowMapper()
                ).getTitle()
        );
    }

    @Test
    void findAllPosts() {
        UpdateResult result = template.update(
                "INSERT INTO users (userId, password, name, email) VALUES (?, ?, ?, ?)",
                List.of("user_id", "password", "name", "email")
        );
        template.update(
                "INSERT INTO posts (authorId, title, content) VALUES (?, ?, ?)",
                List.of(result.getKey(), "title1", "content")
        );
        template.update(
                "INSERT INTO posts (authorId, title, content) VALUES (?, ?, ?)",
                List.of(result.getKey(), "title2", "content")
        );
        template.update(
                "INSERT INTO posts (authorId, title, content) VALUES (?, ?, ?)",
                List.of(result.getKey(), "title3", "content")
        );

        List<Post> posts = subject.findAllPosts();

        assertEquals(
                List.of("title1", "title2", "title3"),
                posts.stream().map(Post::getTitle).collect(Collectors.toList())
        );
    }
}
