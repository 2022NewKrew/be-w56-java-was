package service;

import domain.model.User;
import domain.repository.UserRepository;
import lib.was.di.Bean;
import lib.was.di.Inject;

import java.util.List;

@Bean
public class UserService {

    private final UserRepository repository;

    @Inject
    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    public void addUser(User user) {
        repository.addUser(user);
    }

    public User findUserById(String userId) {
        return repository.findUserById(userId);
    }

    public List<User> findAllUsers() {
        return repository.findAllUsers();
    }
}
