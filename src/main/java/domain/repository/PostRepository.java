package domain.repository;

import domain.model.Post;

import java.util.List;

public interface PostRepository {

    void addPost(Post post);
    List<Post> findAllPosts();
}
