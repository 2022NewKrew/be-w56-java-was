package app.db;

import domain.model.Post;
import domain.repository.PostRepository;
import lib.was.db.JdbcTemplate;
import lib.was.di.Bean;
import lib.was.di.Inject;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Bean
public class JdbcPostRepository implements PostRepository {

    private final JdbcTemplate template;
    private final PostRowMapper mapper;

    @Inject
    public JdbcPostRepository(JdbcTemplate template, PostRowMapper mapper) {
        this.template = template;
        this.mapper = mapper;
    }

    public void addPost(Post post) {
        String sql = "INSERT INTO posts (authorId, title, content) VALUES (?, ?, ?)";
        List<Object> params = List.of(post.getAuthor().getId(), post.getTitle(), post.getContent());
        template.update(sql, params);
    }

    public List<Post> findAllPosts() {
        String sql = "SELECT * FROM posts INNER JOIN users ON posts.authorId = users.id";
        List<Object> params = Collections.emptyList();
        return template.queryForStream(sql, params, mapper).collect(Collectors.toList());
    }
}
