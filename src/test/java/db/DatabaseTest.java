package db;

import model.Post;
import model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import util.PropertiesLoader;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DatabaseTest {

    private JdbcTemplate template;
    private Database subject;

    @BeforeAll
    static void setUpAll() throws IOException {
        PropertiesLoader.load();
    }

    @BeforeEach
    void setUp() {
        template = new JdbcTemplate();
        subject = new Database(template, new UserRowMapper());
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

        User user = subject.findUserById("user_id");

        assertEquals("name", user.getName());
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
