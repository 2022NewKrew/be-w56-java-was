package domain.user.service;

import domain.user.dto.UserLogin;
import domain.user.model.User;
import domain.user.repository.InMemoryUserRepository;
import domain.user.repository.UserRepository;

public class AuthService {

    private final UserRepository userRepository;

    public static AuthService create() {
        return new AuthService(InMemoryUserRepository.get());
    }

    private AuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public boolean login(UserLogin userLogin) {
        User user = userRepository.findUserByUserId(userLogin.getUserId())
            .orElseThrow(() -> new RuntimeException("SOME_MESSAGE"));

        return user.matchPassword(userLogin.getPassword());
    }
}
