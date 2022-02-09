package service;

import domain.model.Post;
import domain.repository.PostRepository;
import lib.was.di.Bean;
import lib.was.di.Inject;

import java.util.List;

@Bean
public class PostService {

    private final PostRepository repository;

    @Inject
    public PostService(PostRepository repository) {
        this.repository = repository;
    }

    public void addPost(Post post) {
        repository.addPost(post);
    }

    public List<Post> findAllPosts() {
        return repository.findAllPosts();
    }
}
