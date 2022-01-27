package factory;

import webserver.domain.repository.UserRepository;
import webserver.domain.repository.UserRepositoryImpl;

public class RepositoryFactory {
    private static final UserRepository userRepository = new UserRepositoryImpl();

    public static UserRepository getUserRepository(){
        return userRepository;
    }
}
