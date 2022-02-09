package domain.user.service;

import domain.user.dto.UserCreate;
import domain.user.dto.UserInfo;
import domain.user.model.User;
import domain.user.repository.H2UserRepository;
import domain.user.repository.UserRepository;
import java.util.List;
import java.util.stream.Collectors;

public class UserService {

    private final UserRepository userRepository;

    public static UserService create() {
        return new UserService(H2UserRepository.get());
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

    public List<UserInfo> readAll() {
        return userRepository.findAll().stream()
            .map(UserInfo::from)
            .collect(Collectors.toUnmodifiableList());
    }
}
