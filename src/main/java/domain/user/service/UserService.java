package domain.user.service;

import domain.user.dto.UserCreate;
import domain.user.model.User;
import domain.user.repository.InMemoryUserRepository;
import domain.user.repository.UserRepository;

public class UserService {

    private final UserRepository userRepository;

    public static UserService create() {
        return new UserService(InMemoryUserRepository.get());
    }

    private UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void create(UserCreate userCreate) {
        User user = User.builder()
            .userId(userCreate.getUserId())
            .password(userCreate.getPassword())
            .name(userCreate.getName())
            .email(userCreate.getEmail())
            .build();
        userRepository.save(user);
    }
}
