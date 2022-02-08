package app.db;

import lib.was.db.JdbcTemplate;
import lib.was.di.Bean;
import lib.was.di.Inject;
import domain.model.Post;
import domain.model.User;

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

    public List<User> findAllUsers() {
        String sql = "SELECT * FROM users";
        List<Object> params = Collections.emptyList();
        return template.queryForStream(sql, params, mapper).collect(Collectors.toList());
    }

    public void addPost(Post post) {
        String sql = "INSERT INTO posts (authorId, title, content) VALUES (?, ?, ?)";
        List<Object> params = List.of(post.getAuthor().getId(), post.getTitle(), post.getContent());
        template.update(sql, params);
    }

    public List<Post> findAllPosts() {
        String sql = "SELECT * FROM posts INNER JOIN users ON posts.authorId = users.id";
        List<Object> params = Collections.emptyList();
        return template.queryForStream(sql, params, new PostRowMapper()).collect(Collectors.toList());
    }
}
