package domain.user.service;

import domain.user.dto.UserCreateRequest;
import domain.user.model.User;
import domain.user.repository.InMemoryUserRepository;
import domain.user.repository.UserRepository;

public class UserService {

    private final UserRepository userRepository;

    public UserService() {
        this.userRepository = new InMemoryUserRepository();
    }

    public void create(UserCreateRequest userCreateRequest) {
        User user = User.builder()
            .userId(userCreateRequest.getUserId())
            .password(userCreateRequest.getPassword())
            .name(userCreateRequest.getName())
            .email(userCreateRequest.getEmail())
            .build();
        userRepository.save(user);
    }
}
