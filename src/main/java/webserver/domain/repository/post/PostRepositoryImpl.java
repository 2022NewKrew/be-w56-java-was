package webserver.domain.repository.post;

import webserver.domain.entity.Post;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PostRepositoryImpl implements PostRepository{
    private final Set<Post> concurrentHashMap = new HashSet<>();

    @Override
    public void save(Post post) {
        concurrentHashMap.add(post);
    }

    @Override
    public List<Post> getAllPosts() {
        return List.of(concurrentHashMap.toArray(Post[]::new));
    }
}
