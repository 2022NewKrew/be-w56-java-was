package webserver.domain.repository.post;

import webserver.domain.entity.Post;

import java.util.List;

public interface PostRepository {
    void save(Post post);
    List<Post> getAllPosts();
}
