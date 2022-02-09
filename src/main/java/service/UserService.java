package service;

import domain.model.User;
import domain.repository.UserRepository;
import lib.was.di.Bean;
import lib.was.di.Inject;

import java.util.List;
import java.util.Optional;

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

    public Optional<User> login(String userId, String password) {
        Optional<User> user = repository.findUserById(userId);
        boolean ok = user.map(x -> x.getPassword().equals(password)).orElse(false);
        return ok ? user : Optional.empty();
    }

    public List<User> findAllUsers() {
        return repository.findAllUsers();
    }
}
