package factory;

import webserver.domain.repository.UserRepository;
import webserver.domain.repository.UserRepositoryImpl;
import webserver.domain.repository.post.PostRepository;
import webserver.domain.repository.post.PostRepositoryImpl;

public class RepositoryFactory {
    private static final UserRepository userRepository = new UserRepositoryImpl();
    private static final PostRepository postRepository = new PostRepositoryImpl();

    public static UserRepository getUserRepository(){
        return userRepository;
    }

    public static PostRepository getPostRepository() {
        return postRepository;
    }
}
