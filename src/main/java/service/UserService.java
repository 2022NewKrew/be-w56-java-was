package service;

import dto.UserDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import model.User;
import repository.UserRepository;

@Slf4j
@RequiredArgsConstructor
public class UserService {
    private static final UserService INSTANCE = new UserService();

    public static UserService getInstance() {
        return INSTANCE;
    }

    private final UserRepository userRepository;

    private UserService() {
        userRepository = UserRepository.getInstance();
    }

    public void register(UserDto dto) {
        User entity = dtoToEntity(dto);
        userRepository.save(entity);
    }

    public UserDto getUserById(UserDto dto) {
        return entityToDto(userRepository.findById(dto.getUserId()));
    }

    public void update(UserDto dto) {
        User entity = dtoToEntity(dto);
        userRepository.update(entity);
    }

    private User dtoToEntity(UserDto dto) {
        return User.builder()
                .userId(dto.getUserId())
                .password(dto.getPassword())
                .name(dto.getName())
                .email(dto.getEmail())
                .build();
    }

    private UserDto entityToDto(User entity) {
        return UserDto.builder()
                .userId(entity.getUserId())
                .password(entity.getPassword())
                .name(entity.getName())
                .email(entity.getEmail())
                .build();
    }
}
