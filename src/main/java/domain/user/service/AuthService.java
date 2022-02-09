package domain.user.service;

import domain.user.dto.UserLogin;
import domain.user.model.User;
import domain.user.repository.H2UserRepository;
import domain.user.repository.UserRepository;
import java.util.Optional;

public class AuthService {

    private final UserRepository userRepository;

    public static AuthService create() {
        return new AuthService(H2UserRepository.get());
    }

    private AuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public boolean login(UserLogin userLogin) {
        Optional<User> foundUser = userRepository.findUserByUserId(userLogin.getUserId());

        return foundUser.isPresent() && foundUser.get().matchPassword(userLogin.getPassword());
    }
}
