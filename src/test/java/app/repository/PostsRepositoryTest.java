package app.repository;

import app.configure.DbConfigure;
import app.model.Post;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class PostsRepositoryTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(PostsRepositoryTest.class);

    private final PostsRepository postsRepository;

    public PostsRepositoryTest() throws SQLException {
        postsRepository = new PostsRepository(new DbConfigure().getConnection());
    }

    @Test
    void insertAndFind() throws SQLException {
        Post post = new Post(LocalDateTime.now(), -1, "yunyul", "hello world");
        long curId = postsRepository.insert(post);
        Post newPost = postsRepository.findById(curId);
        LOGGER.debug("newPost.createdDate : {}", newPost.getCreatedDate());
        assertThat(newPost.getId()).isEqualTo(curId);
    }

}
